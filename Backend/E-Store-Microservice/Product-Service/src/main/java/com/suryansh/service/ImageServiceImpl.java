package com.suryansh.service;

import com.suryansh.entity.Product;
import com.suryansh.entity.ProductImages;
import com.suryansh.exception.SpringProductException;
import com.suryansh.repository.ProductImagesRepository;
import com.suryansh.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{
    private final String FOLDER_PATH="/home/suryansh/Desktop/E-Store_Project/Backend/E-Store-Microservice/ProductImages/";
    private final ProductRepository productRepository;
    private final ProductImagesRepository productImagesRepository;
    @Override
    @Transactional
    public String uploadImageToFileSystem(MultipartFile file, String productName, String isPrimary) throws IOException {
        String filePath=FOLDER_PATH+file.getOriginalFilename();
        file.transferTo(new File(filePath));
        try{
        Product product = productRepository.findByProductName(productName)
                .orElseThrow(()->new SpringProductException("Unable to find Product for upload image"));
        if (isPrimary.equals("Yes")){
            product.setProductImage(file.getOriginalFilename());
            productRepository.save(product);
        }
        ProductImages productImage = ProductImages.builder()
                .productId(product.getId())
                .imageName(file.getOriginalFilename())
                .build();
        productImagesRepository.save(productImage);
        return "file uploaded successfully : " + filePath;
        }catch (Exception e){
            System.out.println(e);
            throw new SpringProductException("Unable to save image for product");
        }

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
