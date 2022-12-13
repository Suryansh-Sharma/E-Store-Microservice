package com.suryansh.userservice.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductDto {
    private Long id;
    private String productName;
    private int ratings;
    private int noOfRatings;
    private String text;
    private Float price;
    private int discount;
    private Float newPrice;
    private String productImage;
    private String productCategory;
    private Boolean productIsInStock;
}
