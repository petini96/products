package br.com.roboticsmind.products.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Fail to upload file")
public class FileUploadException extends RuntimeException {
    public FileUploadException(String message, Exception cause) {
        super(message, cause);
    }
}