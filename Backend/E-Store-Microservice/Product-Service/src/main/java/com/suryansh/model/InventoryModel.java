package com.suryansh.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InventoryModel {
    private String productName;
    private int noOfStock;
    private int totalSold;
}
