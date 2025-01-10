package br.com.roboticsmind.products.service;

import br.com.roboticsmind.products.dto.product.CreateFullProductInputDTO;

public interface IProductService {
    public void registerProduct(CreateFullProductInputDTO createFullProductInputDTO);
}
