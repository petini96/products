package br.com.roboticsmind.products.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PostNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -8790211652911971729L;

    public PostNotFoundException(Long postId) {
        super("Post not found with ID: " + postId);
    }
}