package br.com.roboticsmind.products.controllers;

import br.com.roboticsmind.products.dto.product.CreateFullProductInputDTO;
import br.com.roboticsmind.products.dto.product.ListAllProductsDTO;
import br.com.roboticsmind.products.models.Product;
import br.com.roboticsmind.products.services.IProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@SpringBootApplication
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @GetMapping
    public Page<ListAllProductsDTO> listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return this.iProductService.listProducts(page, size);
    }

    @GetMapping("/{productId}")
    public Product getProduct(@PathVariable Long productId) {
        return this.iProductService.getProduct(productId);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Product createProduct(
            @RequestPart("product") CreateFullProductInputDTO createFullProductInputDTO,
            @RequestPart("photos") List<MultipartFile> photos) {

        System.out.println("Recebendo produto: " + createFullProductInputDTO);
        System.out.println("Recebendo imagens: " + photos.size());

        return this.iProductService.createProduct(createFullProductInputDTO, photos);
    }
}
