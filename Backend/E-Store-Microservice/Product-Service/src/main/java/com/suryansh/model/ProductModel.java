package com.suryansh.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {
    private String productName;
    private int ratings;
    private int noOfRatings;
    private int noOfStock;
    private String text;
    private Float price;
    private int discount;
    private Float newPrice;
    private String productImage;
    private String productCategory;
    // For Description.
    private String description;
    // For Brand.
    private String brandName;
}
