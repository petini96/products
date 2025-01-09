package br.com.roboticsmind.products.dto.product;

import java.math.BigDecimal;
import java.util.List;

import br.com.roboticsmind.products.model.Product;
import br.com.roboticsmind.products.model.ProductCategory;
import br.com.roboticsmind.products.model.ProductPhoto;
import br.com.roboticsmind.products.model.ProductType;

@lombok.Data
@lombok.NoArgsConstructor
public class GetFullProductDTO {

    private Long id;
    private String name;
    private String brand;
    private BigDecimal originalPrice;
    private BigDecimal discountPercentage;
    private BigDecimal discountPrice;
    private BigDecimal installmentPrice;
    private Integer installmentsCount;
    private String additionalInfo;
    private String description;
    private String link;
    private ProductType productType;
    private ProductCategory productCategory;
    private List<ProductPhoto> photos;

    public GetFullProductDTO(Product product){
        this.id = product.getId();
        this.name = product.getName();
        this.brand = product.getBrand();
        this.originalPrice = product.getOriginalPrice();
        this.discountPercentage = product.getDiscountPercentage();
        this.discountPrice = product.getDiscountPrice();
        this.installmentPrice = product.getInstallmentPrice();
        this.installmentsCount = product.getInstallmentsCount();
        this.additionalInfo = product.getAdditionalInfo();
        this.description = product.getDescription();
        this.link = product.getLink();
        this.productType = product.getProductType();
        this.productCategory = product.getProductCategory();
        this.photos = product.getPhotos();
    }   
}
