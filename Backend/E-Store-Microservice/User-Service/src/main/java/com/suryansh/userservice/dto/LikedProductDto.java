package com.suryansh.userservice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LikedProductDto {
    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
}
