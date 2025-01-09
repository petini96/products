package br.com.roboticsmind.products.controller;

import br.com.roboticsmind.products.model.Product;
import br.com.roboticsmind.products.repositories.ProductRepository;
import br.com.roboticsmind.products.service.MinioService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;

@RestController
@SpringBootApplication
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final MinioService minioService;

    public ProductController(
            ProductRepository productRepository,
            MinioService minioService
    ){
        this.productRepository = productRepository;
        this.minioService = minioService;
    }

    @GetMapping("/{productId}")
    public Product getProduct(@PathVariable Long productId) {

        try{
            File file = new File(
                    getClass()
                            .getClassLoader()
                            .getResource("example.png")
                            .getFile()
            );

            FileInputStream fileInputStream = new FileInputStream(file);
            this.minioService.uploadToBucket("products", "example.png", fileInputStream, "image/png");
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

         return this.productRepository.findById(productId).get();
    }
    
}
