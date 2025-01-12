package br.com.roboticsmind.products.services.impl;

import br.com.roboticsmind.products.dto.product.CreateFullProductInputDTO;
import br.com.roboticsmind.products.dto.product.ListAllProductsDTO;
import br.com.roboticsmind.products.models.Product;
import br.com.roboticsmind.products.models.ProductPhoto;
import br.com.roboticsmind.products.repositories.ProductPhotoRepository;
import br.com.roboticsmind.products.repositories.ProductRepository;
import br.com.roboticsmind.products.services.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.roboticsmind.products.services.IProductService;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductPhotoRepository productPhotoRepository;

    @Autowired
    private MinioService minioService;

    @Override
    public Product createProduct(CreateFullProductInputDTO createFullProductInputDTO, List<MultipartFile> photos) {
        try {
            Product newProduct = new Product();
            newProduct.setName(createFullProductInputDTO.getName());
            newProduct.setBrand(createFullProductInputDTO.getBrand());
            newProduct.setOriginalPrice(createFullProductInputDTO.getOriginalPrice());
            newProduct.setDiscountPercentage(createFullProductInputDTO.getDiscountPercentage());
            newProduct.setDiscountPrice(createFullProductInputDTO.getDiscountPrice());
            newProduct.setInstallmentPrice(createFullProductInputDTO.getInstallmentPrice());
            newProduct.setInstallmentsCount(createFullProductInputDTO.getInstallmentsCount());
            newProduct.setAdditionalInfo(createFullProductInputDTO.getAdditionalInfo());
            newProduct.setDescription(createFullProductInputDTO.getDescription());
            newProduct.setLink(createFullProductInputDTO.getLink());
            newProduct.setProductType(createFullProductInputDTO.getProductType());
            newProduct.setProductCategory(createFullProductInputDTO.getProductCategory());

            this.productRepository.save(newProduct);

            for (MultipartFile photo : photos) {
                String photoName = photo.getOriginalFilename();
                try (InputStream photoStream = photo.getInputStream()) {
                    this.minioService.uploadToBucket("products", photoName, photoStream, photo.getContentType());
                }

                ProductPhoto productPhoto = new ProductPhoto();
                productPhoto.setUrl(photoName);
                productPhoto.setProduct(newProduct);
                this.productPhotoRepository.save(productPhoto);
            }

            return newProduct;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar o produto e as imagens", e);
        }
    }

    @Override
    public Product getProduct(Long productId) {
        return this.productRepository.findById(productId).get();
    }

    @Override
    public Page<ListAllProductsDTO> listProducts(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return this.productRepository.findAllProductsDTO(pageable);
    }

}