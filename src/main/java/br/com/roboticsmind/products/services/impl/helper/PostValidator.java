package br.com.roboticsmind.products.services.impl.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import br.com.roboticsmind.products.dto.post.CreatePostDTO;

@Slf4j
public class PostValidator {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public static void validate(CreatePostDTO dto, MultipartFile photo, MultipartFile photoMobile) {
        validatePostDTO(dto);
        validateFile(photo, "Main photo");
        validateFile(photoMobile, "Mobile photo");
    }

    private static void validatePostDTO(CreatePostDTO dto) {
        if (dto == null || dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new IllegalArgumentException("Post title cannot be null or empty");
        }
    }

    private static void validateFile(MultipartFile file, String fileLabel) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(fileLabel + " is required");
        }
        if (file.getOriginalFilename() != null && !file.getOriginalFilename().matches("(?i).+\\.(jpg|jpeg|png|gif|webp)")) {
            throw new IllegalArgumentException(fileLabel + " must be an image (jpg, png, gif, webp)");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(fileLabel + " exceeds size limit of 5MB");
        }
    }
}