package br.com.roboticsmind.products.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Fail to save post")
public class PostSaveException extends RuntimeException {
    public PostSaveException(String message, Exception cause) {
        super(message, cause);
    }
}