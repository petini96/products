package br.com.roboticsmind.products.dto.landingpage;

import br.com.roboticsmind.products.dto.block.BlockDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProductLandingPageDTO {
    private Long id;
    private String pageTitle;
    private String slug;
    private String status;
    private ProductDTO product;
    private List<BlockDTO> blocks;
}