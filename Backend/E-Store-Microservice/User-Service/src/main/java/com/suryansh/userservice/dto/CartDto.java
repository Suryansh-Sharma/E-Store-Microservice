package com.suryansh.userservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CartDto {
    List<CartItems> cartProduct;
    private int totalProducts;
    private Float totalPrice;
}
