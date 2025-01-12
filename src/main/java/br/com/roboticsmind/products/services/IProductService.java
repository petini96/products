package br.com.roboticsmind.products.services;

import br.com.roboticsmind.products.dto.product.CreateFullProductInputDTO;
import br.com.roboticsmind.products.dto.product.ListAllProductsDTO;
import br.com.roboticsmind.products.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    public Product createProduct(CreateFullProductInputDTO createFullProductInputDTO, List<MultipartFile> photos);
    public Product getProduct(Long productId);
    public Page<ListAllProductsDTO> listProducts(int page, int size);
}
