package br.com.roboticsmind.products.services.impl.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import br.com.roboticsmind.products.services.IStorageService;

@Service
@Profile("dev")
public class LocalFileSystemServiceImpl implements IStorageService {

    private final String basePath;
    private final String bucket;
    private final String postsPrefix;
    private final String productsPrefix;

    public LocalFileSystemServiceImpl(
            @Value("${storage.local.path:./uploads}") String basePath,
            @Value("${storage.bucket:mycommerce-roboticsmind-prod-media}") String bucket,
            @Value("${storage.prefix.posts:posts/}") String postsPrefix,
            @Value("${storage.prefix.products:products/}") String productsPrefix) {
        this.basePath = basePath;
        this.bucket = bucket;
        this.postsPrefix = postsPrefix.endsWith("/") ? postsPrefix : postsPrefix + "/";
        this.productsPrefix = productsPrefix.endsWith("/") ? productsPrefix : productsPrefix + "/";
        ensureDirectoryExists();
    }

    @Override
    public String uploadFile(String bucket, String fileName, InputStream stream, String contentType) {
        try {
            String effectiveBucket = (bucket != null && !bucket.isEmpty()) ? bucket : this.bucket;
            String prefix = determinePrefix(fileName);
            String fullPath = Paths.get(basePath, effectiveBucket, prefix, fileName).toString();
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

            return Paths.get(effectiveBucket, prefix, fileName).toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + fileName, e);
        }
    }

    @Override
    public void deleteFile(String bucket, String fileName) {
        try {
            String effectiveBucket = (bucket != null && !bucket.isEmpty()) ? bucket : this.bucket;
            String prefix = determinePrefix(fileName);
            String fullPath = Paths.get(basePath, effectiveBucket, prefix, fileName).toString();
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

    private String determinePrefix(String fileName) {
        if (fileName.startsWith("post-") || fileName.contains("/posts/")) {
            return postsPrefix;
        } else if (fileName.startsWith("product-") || fileName.contains("/products/")) {
            return productsPrefix;
        }
        return "";
    }
}