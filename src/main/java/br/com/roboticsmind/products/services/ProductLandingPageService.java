package br.com.roboticsmind.products.services;

import br.com.roboticsmind.products.dto.block.BlockDTO;
import br.com.roboticsmind.products.dto.block.BlockMetadataDTO;
import br.com.roboticsmind.products.dto.editor.EditorBlockDTO;
import br.com.roboticsmind.products.dto.editor.EditorLandingPageDTO;
import br.com.roboticsmind.products.dto.landingpage.PageBlockDto;
import br.com.roboticsmind.products.dto.landingpage.ProductDTO;
import br.com.roboticsmind.products.dto.landingpage.ProductLandingPageDTO;
import br.com.roboticsmind.products.dto.landingpage.SaveLandingPageRequestDto;
import br.com.roboticsmind.products.models.BlockMetadata;
import br.com.roboticsmind.products.models.PageBlock;
import br.com.roboticsmind.products.models.Product;
import br.com.roboticsmind.products.models.ProductLandingPage;
import br.com.roboticsmind.products.repositories.PageBlockRepository;
import br.com.roboticsmind.products.repositories.ProductLandingPageRepository;
import br.com.roboticsmind.products.repositories.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductLandingPageService {

    private final ProductRepository productRepository;
    private final ProductLandingPageRepository landingPageRepository;
    private final PageBlockRepository pageBlockRepository;
    private final ObjectMapper objectMapper;

    public record SaveResult(ProductLandingPage page, boolean wasCreated) {}

    @Transactional
    public SaveResult saveOrUpdate(Long productId, SaveLandingPageRequestDto dto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com id: " + productId));

        ProductLandingPage page = landingPageRepository.findByProductId(productId).orElse(new ProductLandingPage());
        boolean wasCreated = page.getId() == null;

        page.setProduct(product);
        page.setPageTitle(dto.pageTitle());
        page.setSlug(dto.slug());
        page.setStatus(ProductLandingPage.PageStatus.valueOf(dto.status().toUpperCase()));

        page.getBlocks().clear();
        landingPageRepository.flush(); // Garante que a limpeza seja sincronizada

        if (dto.blocks() != null) {
            int order = 1;
            for (PageBlockDto blockDto : dto.blocks()) {
                PageBlock block = new PageBlock();
                block.setLandingPage(page);
                block.setBlockType(blockDto.type());
                block.setBlockOrder(order++);
                JsonNode data = blockDto.data();

                // LÓGICA DE SALVAMENTO CORRIGIDA E EXPLÍCITA
                switch (blockDto.type()) {
                    case "header":
                        block.setContent(safeGetText(data, "text"));
                        block.setMetadata(new BlockMetadata(safeGetInt(data, "level", 1), null, null));
                        break;
                    case "paragraph":
                        block.setContent(safeGetText(data, "text"));
                        block.setMetadata(null);
                        break;
                    case "image":
                        block.setContent(safeGetText(data.path("file"), "url"));
                        block.setMetadata(new BlockMetadata(null, safeGetText(data, "caption"), null));
                        break;
                    case "list":
                        block.setContent(data.path("items").toString());
                        block.setMetadata(new BlockMetadata(null, null, safeGetText(data, "style", "unordered")));
                        break;
                    case "embed":
                    case "link":
                        try {
                            block.setContent(objectMapper.writeValueAsString(data));
                        } catch (JsonProcessingException e) {
                            block.setContent("{}"); // Salva um JSON vazio em caso de erro
                        }
                        block.setMetadata(null); // Metadados de verdade estão no 'content'
                        break;
                }
                page.getBlocks().add(block);
            }
        }
        ProductLandingPage savedPage = landingPageRepository.save(page);
        return new SaveResult(savedPage, wasCreated);
    }

    @Transactional(readOnly = true)
    public EditorLandingPageDTO findForEditorByProductId(Long productId) {
        ProductLandingPage entity = landingPageRepository.findByProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException("Página não encontrada para o produto com id: " + productId));

        EditorLandingPageDTO editorDto = new EditorLandingPageDTO();
        editorDto.setPageTitle(entity.getPageTitle());
        editorDto.setSlug(entity.getSlug());
        editorDto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : "DRAFT");

        List<EditorBlockDTO> editorBlocks = Optional.ofNullable(entity.getBlocks()).orElse(Collections.emptyList())
                .stream()
                .sorted(Comparator.comparing(PageBlock::getBlockOrder))
                .map(blockEntity -> {
                    EditorBlockDTO blockDto = new EditorBlockDTO();
                    blockDto.setId(String.valueOf(blockEntity.getId()));
                    blockDto.setType(blockEntity.getBlockType());
                    try {
                        // LÓGICA DE RECONSTRUÇÃO PARA O EDITOR CORRIGIDA
                        switch (blockEntity.getBlockType()) {
                            case "header":
                                blockDto.setData(objectMapper.createObjectNode()
                                        .put("text", blockEntity.getContent())
                                        .put("level", blockEntity.getMetadata().level()));
                                break;
                            case "paragraph":
                                blockDto.setData(objectMapper.createObjectNode().put("text", blockEntity.getContent()));
                                break;
                            case "image":
                                ObjectNode fileNode = objectMapper.createObjectNode().put("url", blockEntity.getContent());
                                blockDto.setData(objectMapper.createObjectNode()
                                        .set("file", fileNode));
                                // Adicione outros metadados aqui se o editor precisar
                                break;
                            default: // Para list, embed, link, o 'content' já é o JSON do 'data'
                                blockDto.setData(objectMapper.readTree(blockEntity.getContent()));
                                break;
                        }
                    } catch (JsonProcessingException e) {
                        blockDto.setData(objectMapper.createObjectNode().put("text", "ERRO: Bloco de dados corrompido."));
                    }
                    return blockDto;
                }).collect(Collectors.toList());

        editorDto.setBlocks(editorBlocks);
        return editorDto;
    }

    @Transactional(readOnly = true)
    public ProductLandingPageDTO findDtoBySlug(String slug) {
        ProductLandingPage entity = landingPageRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Página não encontrada com o slug: " + slug));
        return mapToDto(entity);
    }

    // LÓGICA DE MAPEAMENTO PARA PÁGINA PÚBLICA CORRIGIDA E ROBUSTA
    public ProductLandingPageDTO mapToDto(ProductLandingPage entity) {
        ProductDTO productDTO = new ProductDTO();
        if (entity.getProduct() != null) {
            productDTO.setId(entity.getProduct().getId());
            productDTO.setName(entity.getProduct().getName());
        }

        List<BlockDTO> blockDTOs = Optional.ofNullable(entity.getBlocks()).orElse(Collections.emptyList())
                .stream()
                .sorted(Comparator.comparing(PageBlock::getBlockOrder))
                .map(blockEntity -> {
                    BlockDTO blockDTO = new BlockDTO();
                    blockDTO.setId(blockEntity.getId());
                    blockDTO.setBlockType(blockEntity.getBlockType());
                    BlockMetadataDTO metadataDTO = new BlockMetadataDTO();

                    try {
                        switch(blockEntity.getBlockType()) {
                            case "embed":
                                JsonNode embedData = objectMapper.readTree(blockEntity.getContent());
                                blockDTO.setContent(safeGetText(embedData, "embed"));
                                metadataDTO.setCaption(safeGetText(embedData, "caption"));
                                break;
                            case "link":
                                JsonNode linkData = objectMapper.readTree(blockEntity.getContent());
                                blockDTO.setContent(safeGetText(linkData, "link"));
                                metadataDTO.setMeta(objectMapper.convertValue(linkData.path("meta"), Map.class));
                                break;
                            case "list": // Para listas, o conteúdo já é a string JSON dos itens
                                blockDTO.setContent(blockEntity.getContent());
                                if(blockEntity.getMetadata() != null) metadataDTO.setStyle(blockEntity.getMetadata().style());
                                break;
                            case "image": // Para imagens, o content é a URL
                                blockDTO.setContent(blockEntity.getContent());
                                if(blockEntity.getMetadata() != null) metadataDTO.setCaption(blockEntity.getMetadata().caption());
                                break;
                            default: // Para header, paragraph e outros
                                blockDTO.setContent(blockEntity.getContent());
                                if (blockEntity.getMetadata() != null) metadataDTO.setLevel(blockEntity.getMetadata().level());
                                break;
                        }
                    } catch (Exception e) {
                        // Se algo der errado, não quebra a página inteira
                        blockDTO.setContent("");
                    }
                    blockDTO.setMetadata(metadataDTO);
                    return blockDTO;
                }).collect(Collectors.toList());

        ProductLandingPageDTO dto = new ProductLandingPageDTO();
        dto.setId(entity.getId());
        dto.setPageTitle(entity.getPageTitle());
        dto.setSlug(entity.getSlug());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : "DRAFT");
        dto.setProduct(productDTO);
        dto.setBlocks(blockDTOs);
        return dto;
    }

    // Substitua os métodos auxiliares no final da classe ProductLandingPageService por estes:

    private String safeGetText(JsonNode node, String fieldName, String defaultValue) {
        return (node != null && node.has(fieldName) && !node.get(fieldName).isNull())
                ? node.get(fieldName).asText(defaultValue)
                : defaultValue;
    }

    private String safeGetText(JsonNode node, String fieldName) {
        return safeGetText(node, fieldName, ""); // O padrão agora é uma string vazia
    }

    private int safeGetInt(JsonNode node, String fieldName, int defaultValue) {
        return (node != null && node.has(fieldName) && !node.get(fieldName).isNull())
                ? node.get(fieldName).asInt(defaultValue)
                : defaultValue;
    }
}