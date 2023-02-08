package com.suryansh.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubProductModel {
    @NotNull(message = "ProductId can't be empty")
    private Long productId;
    @Size(max = 50,message = "Sub Product text can't be greater than 50 words")
    private String text;
    @NotNull(message = "SubProduct Name can't be empty")
    private String subProductName;
    @NotNull(message = "Product Category can't be empty")
    private String productCategory;
    @NotNull(message = "Brand Name can't be empty")
    private String brandName;
    @NotNull(message = "NoOfStock can't be empty")
    private int noOfStock;
    @NotNull(message = "SubProduct price can't be empty")
    private Float price;
    private int discount;
    private Float newPrice;
    private String productImage;
    private String description;
}
