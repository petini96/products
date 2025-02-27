package br.com.roboticsmind.products.services.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.roboticsmind.products.services.IStorageService;

@Service
public class LocalFileSystemService implements IStorageService {

    private final String basePath;

    public LocalFileSystemService(@Value("${storage.local.path:./uploads}") String basePath) {
        this.basePath = basePath;
        ensureDirectoryExists();
    }

    @Override
    public String uploadFile(String bucket, String fileName, InputStream stream, String contentType) {
        try {

            String fullPath = Paths.get(basePath, bucket, fileName).toString();
            Path path = Paths.get(fullPath).getParent();

            if (path != null) {
                Files.createDirectories(path);
            }

            try (OutputStream outputStream = new FileOutputStream(fullPath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = stream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            return Paths.get(bucket, fileName).toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + fileName, e);
        }
    }

    @Override
    public void deleteFile(String bucket, String fileName) {
        try {
            String fullPath = Paths.get(basePath, bucket, fileName).toString();
            File file = new File(fullPath);
            if (file.exists()) {
                if (!file.delete()) {
                    throw new RuntimeException("Failed to delete file: " + fileName);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file: " + fileName, e);
        }
    }

    private void ensureDirectoryExists() {
        try {
            Files.createDirectories(Paths.get(basePath));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create base directory: " + basePath, e);
        }
    }
}