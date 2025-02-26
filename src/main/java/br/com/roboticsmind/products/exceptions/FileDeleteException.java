package br.com.roboticsmind.products.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Fail to delete file")
public class FileDeleteException extends RuntimeException {
    public FileDeleteException(String message, Exception cause) {
        super(message, cause);
    }
}