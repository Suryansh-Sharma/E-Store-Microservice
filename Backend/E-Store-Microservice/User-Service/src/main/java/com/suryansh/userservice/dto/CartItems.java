package com.suryansh.userservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItems {
    private Long id;
    private Boolean isInStock ;
    private int noOfStock;
    private Long productId;
    private String productName;
    private String productImage;
    private String imageUrl;
    private Float price;
    private Float totalPrice;
    private int noOfProduct;
}
