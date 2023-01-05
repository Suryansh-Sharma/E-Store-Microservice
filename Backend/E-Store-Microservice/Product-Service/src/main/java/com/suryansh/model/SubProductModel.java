package com.suryansh.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubProductModel {
    private Long productId;
    private String text;
    private String subProductName;
    private String productCategory;
    private String brandName;
    private int noOfStock;
    private Float price;
    private int discount;
    private Float newPrice;
    private String productImage;
    private String description;
}
