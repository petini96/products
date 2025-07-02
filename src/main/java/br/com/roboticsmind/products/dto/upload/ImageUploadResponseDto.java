package br.com.roboticsmind.products.dto.upload;

import java.util.Map;

// DTO para a resposta que o Editor.js espera
public record ImageUploadResponseDto(int success, Map<String, String> file) {
    public static ImageUploadResponseDto success(String url) {
        return new ImageUploadResponseDto(1, Map.of("url", url));
    }
}