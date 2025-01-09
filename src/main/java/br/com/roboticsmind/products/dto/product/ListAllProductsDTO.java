package br.com.roboticsmind.products.dto.product;

import lombok.Data;

@Data
public class ListAllProductsDTO {

    private Long id;
    private String name;

    public ListAllProductsDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
