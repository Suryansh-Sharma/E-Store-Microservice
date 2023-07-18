package com.suryansh.orderservice.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartDto {
    private List<CartItem> cartProduct;
    private int totalProducts;
    private Float totalPrice;

}
