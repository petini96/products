package br.com.roboticsmind.products.services.impl.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.roboticsmind.products.annotations.LogExecutionTime;
import br.com.roboticsmind.products.exceptions.FileUploadException;
import br.com.roboticsmind.products.services.IStorageService;

@Service
public class FileUploadHandler {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadHandler.class);
    private final IStorageService storageService;
    private final String bucketName;

    public FileUploadHandler(IStorageService storageService, @Value("${storage.bucket.posts:posts}") String bucketName) {
        this.storageService = storageService;
        this.bucketName = bucketName;
    }

    @LogExecutionTime
    public String uploadFile(MultipartFile file, List<String> uploadedFiles) {
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = UUID.randomUUID() + "-" + originalFileName;

        try (InputStream stream = file.getInputStream()) {
            String url = storageService.uploadFile(bucketName, uniqueFileName, stream, file.getContentType());
            uploadedFiles.add(uniqueFileName);
            logger.info("File uploaded successfully: {} to bucket: {}", uniqueFileName, bucketName);
            return url;
        } catch (IOException e) {
            throw new FileUploadException("Failed to process file: " + uniqueFileName, e);
        } catch (Exception e) {
            throw new FileUploadException("Failed to upload file to storage: " + uniqueFileName, e);
        }
    }

    @LogExecutionTime
    public void cleanupFiles(List<String> fileNames) {
        if (fileNames.isEmpty()) {
            return;
        }
        for (String fileName : fileNames) {
            try {
                storageService.deleteFile(bucketName, fileName);
                logger.info("Cleaned up file: {} from bucket: {}", fileName, bucketName);
            } catch (Exception e) {
                logger.error("Failed to delete file during cleanup: {} from bucket: {}", fileName, bucketName, e);
            }
        }
    }
}