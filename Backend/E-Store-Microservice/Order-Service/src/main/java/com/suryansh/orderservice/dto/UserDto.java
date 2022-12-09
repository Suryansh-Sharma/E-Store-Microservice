package com.suryansh.orderservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {
    private Long id;
    private String userName;
    private int cartTotalProducts;
    private Float cartTotalPrice;
    private int totalLikedProduct;
}
