package com.suryansh.dto;

import lombok.*;
import org.springframework.stereotype.Service;

@Builder
@Getter
@Setter
@Service
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDto {
    private Long id;
    private Long productId;
    private String imageName;
    private String imageUrl;


    public void setImageUrl(String imageName) {
        this.imageUrl = "http://localhost:8080/api/image/download/"+imageName;
    }
}
