package br.com.roboticsmind.products.services.impl.storage;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import br.com.roboticsmind.products.services.IStorageService;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;

@Service
@Profile("local")
public class MinioServiceImpl implements IStorageService {

    private final MinioClient minioClient;
    private final String bucket;
    private final String postsPrefix;
    private final String productsPrefix;

    public MinioServiceImpl(
            @Value("${minio.url}") String url,
            @Value("${minio.access-key}") String accessKey,
            @Value("${minio.secret-key}") String secretKey,
            @Value("${storage.bucket:mycommerce-roboticsmind-prod-media}") String bucket,
            @Value("${storage.prefix.posts:posts/}") String postsPrefix,
            @Value("${storage.prefix.products:products/}") String productsPrefix) {
        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
        this.bucket = bucket;
        this.postsPrefix = postsPrefix.endsWith("/") ? postsPrefix : postsPrefix + "/";
        this.productsPrefix = productsPrefix.endsWith("/") ? productsPrefix : productsPrefix + "/";
    }

    @Override
    public String uploadFile(String bucket, String fileName, InputStream stream, String contentType) {
        try {
            String effectiveBucket = (bucket != null && !bucket.isEmpty()) ? bucket : this.bucket;
            String prefix = determinePrefix(fileName);
            String fullObjectName = prefix + fileName;
            ensureBucketExists(effectiveBucket);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(effectiveBucket)
                    .object(fullObjectName)
                    .stream(stream, stream.available(), -1)
                    .contentType(contentType)
                    .build());
            return getUrl(effectiveBucket, fullObjectName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + fileName, e);
        }
    }

    @Override
    public void deleteFile(String bucket, String fileName) {
        try {
            String effectiveBucket = (bucket != null && !bucket.isEmpty()) ? bucket : this.bucket;
            String prefix = determinePrefix(fileName);
            String fullObjectName = prefix + fileName;
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(effectiveBucket).object(fullObjectName).build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file: " + fileName, e);
        }
    }

    public boolean makeBucket(String bucket) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private void ensureBucketExists(String bucketName) throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    public String uploadToBucket(String bucketName, String objectName, InputStream stream, String contentType)
            throws Exception {
        String prefix = determinePrefix(objectName);
        String fullObjectName = prefix + objectName;
        ensureBucketExists(bucketName);
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fullObjectName)
                .stream(stream, stream.available(), -1)
                .contentType(contentType)
                .build());
        return getUrl(bucketName, fullObjectName);
    }

    public void deleteObject(String bucketName, String objectName) throws Exception {
        String prefix = determinePrefix(objectName);
        String fullObjectName = prefix + objectName;
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fullObjectName).build());
    }

    public String getUrl(String bucketName, String objectName) throws Exception {
        String prefix = determinePrefix(objectName);
        String fullObjectName = prefix + objectName;
        String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(fullObjectName)
                .expiry(7 * 24 * 60 * 60)
                .build());
        return url;
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