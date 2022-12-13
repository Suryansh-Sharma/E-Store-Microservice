package com.suryansh.dto;

import com.suryansh.entity.Description;
import com.suryansh.entity.SubProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private Description description;
    private Boolean productIsInStock;
    private BrandDto brand;
    private List<ProductImageDto>productImages;
    private List<SubProductDto> subProducts;
}
