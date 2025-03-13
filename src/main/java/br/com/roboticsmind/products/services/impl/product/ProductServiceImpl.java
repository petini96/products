package br.com.roboticsmind.products.services.impl.product;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.roboticsmind.products.dto.product.CreateFullProductInputDTO;
import br.com.roboticsmind.products.dto.product.ListAllProductsDTO;
import br.com.roboticsmind.products.models.Product;
import br.com.roboticsmind.products.models.ProductPhoto;
import br.com.roboticsmind.products.repositories.ProductPhotoRepository;
import br.com.roboticsmind.products.repositories.ProductRepository;
import br.com.roboticsmind.products.services.IProductService;
import br.com.roboticsmind.products.services.IStorageService;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IStorageService storageService;
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductPhotoRepository productPhotoRepository;

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
                    this.storageService.uploadFile("products", photoName, photoStream, photo.getContentType());
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
        Page<ListAllProductsDTO> allProductsDTO = this.productRepository.findAllProductsDTO(pageable);
        allProductsDTO.map(productDTO -> {
            System.out.println("Product Description: " + productDTO.getDescription());
            List<ProductPhoto> productPhotos = productPhotoRepository.findByProductId(productDTO.getId());
            if (productPhotos != null) {
                productPhotos.forEach(photo -> {
                    System.out.println("Photo URL: " + photo.getUrl());
                    System.out.println("Photo File Type: " + photo.getFileType());
                    System.out.println("Photo File Size: " + photo.getFileSize());
                });
                productDTO.setPhotos(productPhotos);
            } else {
                System.out.println("No photos available for this product.");
            }

            return productDTO;
        });

        return allProductsDTO;
    }

}
