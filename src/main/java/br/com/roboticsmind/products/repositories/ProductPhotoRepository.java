package br.com.roboticsmind.products.repositories;

import br.com.roboticsmind.products.models.Product;
import br.com.roboticsmind.products.models.ProductPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductPhotoRepository  extends JpaRepository<ProductPhoto, Long> {
    List<ProductPhoto> findByProductId(long productId);
    Product findById(long id);
}
