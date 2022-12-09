package com.suryansh.orderservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
@Builder
public class CartDto {
    List<CartItems> cartProduct;
    private int totalProducts;
    private Float totalPrice;

}
