package br.com.roboticsmind.products.repositories;

import br.com.roboticsmind.products.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
 
public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByShirt(String shirt);

    Product findById(long id);
}
