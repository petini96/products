package br.com.roboticsmind.products.services;

import java.io.InputStream;

public interface IStorageService {
    String uploadFile(String bucket, String fileName, InputStream stream, String contentType);
    void deleteFile(String bucket, String fileName);
}