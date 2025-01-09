package br.com.roboticsmind.products.repositories;

import br.com.roboticsmind.products.model.Product;
import br.com.roboticsmind.products.model.ProductPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
 
public interface ProductPhotoRepository  extends JpaRepository<ProductPhoto, Long> {
    Product findById(long id);
}
