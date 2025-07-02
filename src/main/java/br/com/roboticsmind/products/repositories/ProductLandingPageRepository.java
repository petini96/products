package br.com.roboticsmind.products.repositories;

import br.com.roboticsmind.products.models.ProductLandingPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductLandingPageRepository extends JpaRepository<ProductLandingPage, Long> {

    // Busca uma landing page pelo slug.
    Optional<ProductLandingPage> findBySlug(String slug);

    // Busca uma landing page pelo ID do produto associado.
    Optional<ProductLandingPage> findByProductId(Long productId);

    // O MÉTODO "deleteByLandingPageId" FOI REMOVIDO DAQUI.
}