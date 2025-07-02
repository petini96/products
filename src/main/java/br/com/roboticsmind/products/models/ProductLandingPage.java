package br.com.roboticsmind.products.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_landing_page")
@Getter
@Setter
public class ProductLandingPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore // O Jackson vai ignorar completamente este campo
    private Product product;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(name = "page_title", nullable = false)
    private String pageTitle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PageStatus status = PageStatus.DRAFT;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @OneToMany(mappedBy = "landingPage", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("blockOrder ASC")
    private List<PageBlock> blocks = new ArrayList<>();

    public enum PageStatus {
        DRAFT,
        PUBLISHED
    }
}