package com.suryansh.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.Instant;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {
    @NotNull(message = "Product name cannot be blank")
    private String productName;
    @NotNull(message = "No of stock can't be empty")
    private int noOfStock;
    @Size(max = 50,message = "Text size should not be greater than 50")
    private String text;
    @NotNull(message = "Price of product can't be empty")
    private Float price;
    private int discount;
    private Float newPrice;
    @NotNull(message = "Product Category can't be empty")
    private String productCategory;
    // For Description.
    private String description;
    @NotNull(message = "Product Brand name can't be empty")
    // For Brand.
    private String brandName;
}
