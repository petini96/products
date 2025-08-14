package br.com.roboticsmind.products.services;

import br.com.roboticsmind.products.dto.category.CreateProductCategoryDTO;
import br.com.roboticsmind.products.dto.category.ListProductCategoryDTO;
import br.com.roboticsmind.products.models.ProductCategory;
import br.com.roboticsmind.products.repositories.ProductCategoryRepository;

import br.com.roboticsmind.products.exceptions.ResourceNotFoundException;
import br.com.roboticsmind.products.exceptions.DuplicateResourceException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository repository;

    public ProductCategoryService(ProductCategoryRepository repository) {
        this.repository = repository;
    }

    private ListProductCategoryDTO toDTO(ProductCategory category) {
        return new ListProductCategoryDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getImageUrl(),
                category.isActive()
        );
    }

    @Transactional(readOnly = true)
    public List<ListProductCategoryDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ListProductCategoryDTO findById(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + id));
    }

    @Transactional
    public ListProductCategoryDTO create(CreateProductCategoryDTO requestDTO) {
        repository.findByName(requestDTO.name()).ifPresent(c -> {
            throw new DuplicateResourceException("Uma categoria com o nome '" + requestDTO.name() + "' já existe.");
        });

        ProductCategory newCategory = new ProductCategory();
        newCategory.setName(requestDTO.name());
        newCategory.setDescription(requestDTO.description());
        newCategory.setImageUrl(requestDTO.imageUrl());
        newCategory.setActive(requestDTO.active());

        ProductCategory savedCategory = repository.save(newCategory);
        return toDTO(savedCategory);
    }

    @Transactional
    public ListProductCategoryDTO update(Long id, CreateProductCategoryDTO requestDTO) {
        ProductCategory category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + id));

        repository.findByName(requestDTO.name()).ifPresent(existingCategory -> {
            if (!existingCategory.getId().equals(id)) {
                throw new DuplicateResourceException("O nome '" + requestDTO.name() + "' já está em uso por outra categoria.");
            }
        });

        category.setName(requestDTO.name());
        category.setDescription(requestDTO.description());
        category.setImageUrl(requestDTO.imageUrl());
        category.setActive(requestDTO.active());

        ProductCategory updatedCategory = repository.save(category);
        return toDTO(updatedCategory);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Não foi possível deletar. Categoria com ID " + id + " não encontrada.");
        }
        repository.deleteById(id);
    }
}