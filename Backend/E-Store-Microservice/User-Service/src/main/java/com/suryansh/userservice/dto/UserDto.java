package com.suryansh.userservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String userName;
    private int cartTotalProducts;
    private Float cartTotalPrice;
    private int totalLikedProduct;
}
