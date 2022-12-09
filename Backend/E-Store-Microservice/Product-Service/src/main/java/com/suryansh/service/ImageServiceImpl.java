package com.suryansh.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService{
    private final String FOLDER_PATH="/home/suryansh/Desktop/E-Store_Project/Backend/E-Store-Microservice/ProductImages/";
    @Override
    public String uploadImageToFileSystem(MultipartFile file) throws IOException {
        String filePath=FOLDER_PATH+file.getOriginalFilename();
        file.transferTo(new File(filePath));
        return "file uploaded successfully : " + filePath;
    }

    @Override
    public byte[] downloadImage(String fileName) throws IOException {
        String fullPath = FOLDER_PATH+"/"+fileName;
        return Files.readAllBytes(new File(fullPath).toPath());
    }

    @Override
    public String updateImage(MultipartFile file, String oldFileName) throws IOException {
        String fullPath = FOLDER_PATH+"/"+oldFileName;
        File fileToBeDeleted = new File(fullPath);
        if (fileToBeDeleted.delete()){
            log.info("File Deleted Successfully");
        }
        String filePath=FOLDER_PATH+file.getOriginalFilename();
        file.transferTo(new File(filePath));
        return "file Updated successfully : " + filePath;
    }

}
