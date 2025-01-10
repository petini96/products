package br.com.roboticsmind.products.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String brand;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal originalPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discountPercentage;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discountPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal installmentPrice;
    
    @Column(nullable = false)
    private Integer installmentsCount;

    @Column(nullable = true)
    private String additionalInfo;
    
    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String link;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_type_id", nullable = false)
    private ProductType productType;

    @ManyToOne
    @JoinColumn(name = "product_category_id", nullable = false)
    private ProductCategory productCategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductPhoto> photos;
     
}
