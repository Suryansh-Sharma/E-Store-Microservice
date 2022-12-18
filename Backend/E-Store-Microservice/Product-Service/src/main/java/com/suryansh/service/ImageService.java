package com.suryansh.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String uploadImageToFileSystem(MultipartFile file, String productName, String isPrimary) throws IOException;

    byte[] downloadImage(String fileName) throws IOException;

    String updateImage(MultipartFile file, String oldFileName) throws IOException;
}
