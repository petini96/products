package br.com.roboticsmind.products.controllers;

import br.com.roboticsmind.products.dto.upload.ImageUploadResponseDto;
import br.com.roboticsmind.products.services.IStorageService;
import com.google.common.io.Files;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/products/{productId}/uploads") // URL agora inclui o ID do produto
@RequiredArgsConstructor
public class ImageUploadController {

    private final IStorageService storageService; // Seu MinioServiceImpl

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImageUploadResponseDto uploadImage(
            @PathVariable Long productId, // Recebe o ID do produto da URL
            @RequestParam("image") MultipartFile file) {
        try {
            // Agora podemos criar um nome de arquivo mais específico
            String extension = Files.getFileExtension(file.getOriginalFilename());
            String fileName = "product-" + productId + "/content-" + UUID.randomUUID().toString() + "." + extension;

            // O seu MinioServiceImpl já tem lógica para usar o prefixo "products/"
            String fileUrl = storageService.uploadFile(null, fileName, file.getInputStream(), file.getContentType());

            return ImageUploadResponseDto.success(fileUrl);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image for product " + productId, e);
        }
    }
}