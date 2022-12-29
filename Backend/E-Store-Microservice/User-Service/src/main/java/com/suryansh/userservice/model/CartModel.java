package com.suryansh.userservice.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class CartModel {
    private Long id;
    private String userName;
    private Boolean isInStock;
    private Long productId;
    private String productName;
    private String productImage;
    private String imageUrl;
    private Float price;
    private Float totalPrice;
    private int noOfProduct;
}
