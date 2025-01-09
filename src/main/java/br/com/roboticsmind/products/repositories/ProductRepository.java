package br.com.roboticsmind.products.repositories;

import br.com.roboticsmind.products.dto.product.ListAllProductsDTO;
import br.com.roboticsmind.products.model.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
 
public interface ProductRepository  extends JpaRepository<Product, Long> {
    @Query("SELECT new br.com.roboticsmind.products.dto.product.ListAllProductsDTO(p.id, p.name) FROM Product p")
    Page<ListAllProductsDTO> findAllProductsDTO(Pageable pageable);

    List<Product> findByName(String name);

    Product findById(long id);
}
