package br.com.roboticsmind.products.repositories;

import br.com.roboticsmind.products.models.PageBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PageBlockRepository extends JpaRepository<PageBlock, Long> {

    /**
     * Deleta todos os blocos que pertencem a uma landing page específica.
     * Esta é a nossa query de exclusão em massa explícita.
     */
    @Modifying
    @Query("DELETE FROM PageBlock p WHERE p.landingPage.id = :landingPageId")
    void deleteByLandingPageId(Long landingPageId);

}