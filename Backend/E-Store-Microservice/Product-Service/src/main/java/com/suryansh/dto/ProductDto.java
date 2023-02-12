package com.suryansh.dto;

import com.suryansh.entity.Description;
import lombok.*;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    private String imageUrl;
    private String productCategory;
    private DescriptionDto description;
    private BrandDto brand;
    private List<ProductImageDto>productImages;
    private Boolean productIsInStock;
    private InventoryResponse inventoryData;
    private List<ProductDto>similarProducts;
}
