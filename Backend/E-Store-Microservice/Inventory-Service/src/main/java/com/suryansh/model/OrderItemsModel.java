package com.suryansh.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItemsModel {
    private String productName;
    private Long productId;
    private int noOfStock;
    private int totalSold;
}
