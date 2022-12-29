package com.suryansh.orderservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {
    List<CartItems> cartProduct;
    private int totalProducts;
    private Float totalPrice;

}
