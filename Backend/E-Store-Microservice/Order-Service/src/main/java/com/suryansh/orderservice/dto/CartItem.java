package com.suryansh.orderservice.dto;

import lombok.*;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private Long id;
    private Boolean isInStock ;
    private Long productId;
    private String productName;
    private String productImage;
    private String imageUrl;
    private Float price;
    private Float totalPrice;
    private int noOfProduct;
}
