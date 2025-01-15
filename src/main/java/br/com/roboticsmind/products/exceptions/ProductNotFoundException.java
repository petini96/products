package br.com.roboticsmind.products.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such product")
public class ProductNotFoundException extends RuntimeException{
    /**
     * Unique ID for Serialized object
     */
    @Serial
    private static final long serialVersionUID = -8790211652911971729L;

    public ProductNotFoundException(Long productId){
        super(productId + " not found");
    }
}
