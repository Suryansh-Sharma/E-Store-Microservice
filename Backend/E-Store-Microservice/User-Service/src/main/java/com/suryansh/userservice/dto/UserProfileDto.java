package com.suryansh.userservice.dto;

import lombok.*;


public class UserDto {
    private Long id;
    private String userName;
    private int cartTotalProducts;
    private Float cartTotalPrice;
    private int totalLikedProduct;
}
