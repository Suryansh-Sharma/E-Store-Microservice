package com.suryansh.controller;

import com.suryansh.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("image")MultipartFile file) throws IOException {
        return imageService.uploadImageToFileSystem(file);
    }
    @GetMapping("download/{fileName}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable String fileName) throws IOException {
        byte[] image = imageService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(image);
    }
    @PutMapping("update/{oldFileName}")
    public String updateImage(@PathVariable String oldFileName,@RequestParam("image")MultipartFile file) throws IOException {
        return imageService.updateImage(file,oldFileName);
    }
}
