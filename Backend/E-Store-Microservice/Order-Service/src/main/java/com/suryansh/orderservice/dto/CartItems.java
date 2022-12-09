package com.suryansh.orderservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItems {
    private Long id;
    private Boolean isInStock = false;
    private Long productId;
    private String productName;
    private Float price;
    private Float totalPrice;
    private int noOfProduct;
}
