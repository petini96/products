package br.com.roboticsmind.products.services.impl.storage;

import java.io.InputStream;
import java.net.URI;

import br.com.roboticsmind.products.services.IStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import java.time.Duration;

@Service
public class S3ServiceImpl implements IStorageService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String region;
    private final String bucket;
    private final String postsPrefix;
    private final String productsPrefix;

    public S3ServiceImpl(
            @Value("${aws.s3.url:}") String url,
            @Value("${aws.access-key}") String accessKey,
            @Value("${aws.secret-key}") String secretKey,
            @Value("${aws.region}") String region,
            @Value("${storage.bucket:mycommerce-roboticsmind-prod-media}") String bucket,
            @Value("${storage.prefix.posts:posts/}") String postsPrefix,
            @Value("${storage.prefix.products:products/}") String productsPrefix) {
        this.region = region;
        this.bucket = bucket;
        this.postsPrefix = postsPrefix.endsWith("/") ? postsPrefix : postsPrefix + "/";
        this.productsPrefix = productsPrefix.endsWith("/") ? productsPrefix : productsPrefix + "/";

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(awsCreds);

        S3ClientBuilder s3ClientBuilder = S3Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.of(region));
        if (!url.isEmpty()) {
            s3ClientBuilder.endpointOverride(URI.create(url));
        }
        this.s3Client = s3ClientBuilder.build();

        S3Presigner.Builder presignerBuilder = S3Presigner.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.of(region));
        if (!url.isEmpty()) {
            presignerBuilder.endpointOverride(URI.create(url));
        }
        this.s3Presigner = presignerBuilder.build();
    }

    @Override
    public String uploadFile(String bucket, String fileName, InputStream stream, String contentType) {
        try {
            String effectiveBucket = (bucket != null && !bucket.isEmpty()) ? bucket : this.bucket;
            String prefix = determinePrefix(fileName);
            String fullKey = prefix + fileName;
            ensureBucketExists(effectiveBucket);
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(effectiveBucket)
                            .key(fullKey)
                            .contentType(contentType)
                            .build(),
                    RequestBody.fromInputStream(stream, stream.available())
            );
            return getUrl(effectiveBucket, fullKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + fileName, e);
        }
    }

    @Override
    public void deleteFile(String bucket, String fileName) {
        try {
            String effectiveBucket = (bucket != null && !bucket.isEmpty()) ? bucket : this.bucket;
            String prefix = determinePrefix(fileName);
            String fullKey = prefix + fileName;
            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(effectiveBucket)
                            .key(fullKey)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file: " + fileName, e);
        }
    }

    private void ensureBucketExists(String bucketName) {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
        } catch (NoSuchBucketException e) {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to ensure bucket exists: " + bucketName, e);
        }
    }

    private String getUrl(String bucketName, String objectName) {
        try {
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofDays(7))
                    .getObjectRequest(getObjectRequest -> getObjectRequest
                            .bucket(bucketName)
                            .key(objectName))
                    .build();

            return s3Presigner.presignGetObject(presignRequest).url().toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate presigned URL for: " + objectName, e);
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