package br.com.roboticsmind.products.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "page_block")
@Getter
@Setter
public class PageBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "landing_page_id", nullable = false)
    private ProductLandingPage landingPage;

    @Column(name = "block_type", nullable = false)
    private String blockType;

    @Lob
    private String content;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private BlockMetadata metadata;

    @Column(name = "block_order", nullable = false)
    private Integer blockOrder;

    @CreationTimestamp
    private Instant createdAt;
}