package br.com.roboticsmind.products.dto.product;

import br.com.roboticsmind.products.models.ProductPhoto;
import lombok.Data;

import java.util.List;

@Data
public class ListAllProductsDTO {

    private Long id;
    private String name;
    private String description;
    private List<ProductPhoto> photos;

    public ListAllProductsDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
