package com.suryansh.userservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Builder
@Data
public class CartDto {
    private List<CartItems> cartProduct;
    private int totalProducts;
    private Float totalPrice;

}

