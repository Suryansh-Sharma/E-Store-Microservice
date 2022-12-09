package com.suryansh.orderservice.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InventoryModel {
    private String productName;
    private Long productId;
    private int noOfStock;
    private int totalSold;
}
