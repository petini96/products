package br.com.roboticsmind.products.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record CreateProductCategoryDTO(
        @NotBlank(message = "O nome da categoria é obrigatório.")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
        String name,

        String description,
        String imageUrl,
        boolean active
) {}