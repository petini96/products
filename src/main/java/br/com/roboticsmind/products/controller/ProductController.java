package br.com.roboticsmind.products.controller;

import br.com.roboticsmind.products.dto.product.CreateFullProductInputDTO;
import br.com.roboticsmind.products.dto.product.ListAllProductsDTO;
import br.com.roboticsmind.products.model.Product;
import br.com.roboticsmind.products.model.ProductPhoto;
import br.com.roboticsmind.products.repositories.ProductPhotoRepository;
import br.com.roboticsmind.products.repositories.ProductRepository;
import br.com.roboticsmind.products.service.MinioService;

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

import java.io.InputStream;
import java.util.List;

@RestController
@SpringBootApplication
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductPhotoRepository productPhotoRepository;

    @Autowired
    private MinioService minioService;

    @GetMapping
    public Page<ListAllProductsDTO> listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return this.productRepository.findAllProductsDTO(pageable);
    }

    @GetMapping("/{productId}")
    public Product getProduct(@PathVariable Long productId) {
        return this.productRepository.findById(productId).get();
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Product createProduct(
            @RequestPart("product") CreateFullProductInputDTO productDTO,
            @RequestPart("photos") List<MultipartFile> photos) {

        System.out.println("Recebendo produto: " + productDTO);
        System.out.println("Recebendo imagens: " + photos.size());
        try {
            
            Product product = new Product();
            product.setName(productDTO.getName());
            product.setBrand(productDTO.getBrand());
            product.setOriginalPrice(productDTO.getOriginalPrice());
            product.setDiscountPercentage(productDTO.getDiscountPercentage());
            product.setDiscountPrice(productDTO.getDiscountPrice());
            product.setInstallmentPrice(productDTO.getInstallmentPrice());
            product.setInstallmentsCount(productDTO.getInstallmentsCount());
            product.setAdditionalInfo(productDTO.getAdditionalInfo());
            product.setDescription(productDTO.getDescription());
            product.setLink(productDTO.getLink());
            product.setProductType(productDTO.getProductType());
            product.setProductCategory(productDTO.getProductCategory());
            productRepository.save(product);

            for (MultipartFile photo : photos) {
                String photoName = photo.getOriginalFilename();
                try (InputStream photoStream = photo.getInputStream()) {
                    minioService.uploadToBucket("products", photoName, photoStream, photo.getContentType());
                }
                
                ProductPhoto productPhoto = new ProductPhoto();
                productPhoto.setUrl(photoName);
                productPhoto.setProduct(product);
                this.productPhotoRepository.save(productPhoto);
            }

            return product;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar o produto e as imagens", e);
        }
    }
}
