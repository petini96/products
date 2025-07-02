package br.com.roboticsmind.products.dto.block;

import lombok.Data;

@Data
public class BlockDTO {
    private Long id;
    private String blockType; // Ex: 'header', 'paragraph', 'image'
    private String content;   // O conteúdo do bloco (texto, URL da imagem, etc)
    private BlockMetadataDTO metadata; // Metadados como level do header, etc.
}