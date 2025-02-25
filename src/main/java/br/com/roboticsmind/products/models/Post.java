package br.com.roboticsmind.products.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Media is mandatory")
    @Column(nullable = false, unique = true)
    private String media;

    @NotBlank(message = "Media mobile is mandatory")
    @Column(nullable = false, name = "media_mobile", unique = true)
    private String mediaMobile;

    @NotBlank(message = "Title is mandatory")
    @Column(nullable = false, unique = true)
    private String title;

    @NotBlank(message = "Description is mandatory")
    @Column(nullable = false)
    private String description;

    @NotNull(message = "Order is mandatory")
    @Positive(message = "Order must be positive")
    @Digits(integer = 2, fraction = 0, message = "Order must be an integer up to 2 digits")
    @Column(nullable = false, name = "\"order\"", unique = true)
    private Integer order;
}