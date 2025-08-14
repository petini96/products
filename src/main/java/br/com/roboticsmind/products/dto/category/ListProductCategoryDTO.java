package br.com.roboticsmind.products.dto.category;

public record ListProductCategoryDTO(
        Long id,
        String name,
        String description,
        String imageUrl,
        boolean active
) {}