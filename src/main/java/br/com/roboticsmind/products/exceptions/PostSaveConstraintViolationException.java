package br.com.roboticsmind.products.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Constraint violation")
public class PostSaveConstraintViolationException extends RuntimeException {
    public PostSaveConstraintViolationException(String message, Exception cause) {
        super(message, cause);
    }
}