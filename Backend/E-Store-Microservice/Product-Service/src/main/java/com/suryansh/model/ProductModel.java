package com.suryansh.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {
    @NotBlank(message = "Product name cannot be null")
    private String productName;
    @NotNull(message = "No of stock can't be empty")
    @Min(value = 1,message = "No of stock can't be less than or equal to zero")
    private int noOfStock;
    private String text;
    @NotNull(message = "Price of product can't be empty")
    private Float price;
    private int discount;
    private Float newPrice;
    @NotBlank(message = "Product Category can't be empty")
    private String productCategory;
    // For Description.
    private String description;
    @NotBlank(message = "Product Brand name can't be empty")
    // For Brand.
    private String brandName;
}
