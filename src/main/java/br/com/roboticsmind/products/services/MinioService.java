package br.com.roboticsmind.products.services;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;

@Service
public class MinioService {

    private final MinioClient minioClient;

    public MinioService(
            @Value("${minio.url}") String url,
            @Value("${minio.access-key}") String accessKey,
            @Value("${minio.secret-key}") String secretKey) {
        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

    public boolean makeBucket(String bucket) {
        try {
            this.minioClient.makeBucket(
                    MakeBucketArgs
                            .builder()
                            .bucket(bucket)
                            .build());
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public String uploadToBucket(String bucketName, String objectName, InputStream stream, String contentType)
            throws Exception {
        this.minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(stream, stream.available(), -1)
                .contentType(contentType)
                .build());

        return this.getUrl(bucketName, objectName);
    }

    public String getUrl(String bucketName, String objectName)
            throws Exception {
        String url = this.minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .expiry(7 * 24 * 60 * 60)
                .build());
        return url;
    }

}