package br.com.roboticsmind.products.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
//    id: "1",
//    name: "iPhone 16 Pro - Brown",
//    category: "Smartphones",
//    brand: "Apple",
//    quantity: 1,
//    originalPrice: "R$ 7.999",
//    discountPercentage: "10%",
//    discountedPrice: "6.999",
//    installmentPrice: "R$ 720",
//    installmentsCount: "12",
//    additionalInfo: "Garanta o seu agora mesmo!",
//    description: "iPhone 16 Pro - Brown, design sofisticado e desempenho de ponta.",
//    photos: [
//            "https://reidocelular.com.br/wp-content/uploads/2024/09/Sem-2024-11-09T060243.299.png",
//            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT5FUp9nSAHSFd_VCfz3zihDtnN3RMoBLvEGFFZIWQ6T2Ap1FLB09UaFMpvZODqcLVVnS0&usqp=CAU",
//            "https://www.cnet.com/a/img/resize/24f33e2947794c06413c0f3cdf90d36755074186/hub/2023/09/12/01a357c3-9eb9-418f-b485-26a5c602203c/iphone-15-pro-titanium.png?auto=webp&fit=crop&height=1200&width=1200",
//            "https://5.imimg.com/data5/SELLER/Default/2024/1/375836308/UP/MK/QR/33769411/lightest-boybgwifuehe-large-2x.jpg"
//            ],
//    link: "iphone-16"
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private BigDecimal installmentPrice;
    
    @Column(nullable = false)
    private Integer installmentsCount;



    @Column(nullable = false)
    private String shirt;

    @Column(nullable = false)
    private String model;

    private String photo;
    private String league;
    
    @Column(length = 1)
    private Character gender;

    @ManyToOne
    private ProductType productType;

    @ManyToOne
    private ProductCategory productCategory;
}
