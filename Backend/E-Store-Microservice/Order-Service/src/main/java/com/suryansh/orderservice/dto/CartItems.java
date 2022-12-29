package com.suryansh.orderservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartItems {
    private Long id;
    private Boolean isInStock;
    private Long productId;
    private String productName;
    private Float price;
    private Float totalPrice;
    private int noOfProduct;
}
