package br.com.roboticsmind.products.service;

import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class MinioService {

    MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://127.0.0.1:9000")
                    .credentials("minio", "minio123")
                    .build();

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

    public boolean uploadToBucket(String bucket, String fileName, FileInputStream fileInputStream, String contentType) {
        try {

            byte[] fileBytes = toByteArray(fileInputStream);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);
            long fileSize = fileBytes.length;


            this.minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName)
                    .stream(byteArrayInputStream, fileSize, -1)
                    .contentType(contentType)
                    .build());

            System.out.println("Arquivo carregado com sucesso!");
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao enviar arquivo para o bucket: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private byte[] toByteArray(FileInputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }

}
