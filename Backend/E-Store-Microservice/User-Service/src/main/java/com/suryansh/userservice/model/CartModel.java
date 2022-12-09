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
    private Long productId;
    private int noOfProduct;
}
