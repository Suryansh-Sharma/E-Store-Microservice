package com.suryansh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductFullDto {
    private Long id;
    private String title;
    private String shortDescription;
    // Price
    private Price price;
    // Discount Price
    private DiscountPrice discountPrice;

    private String categoryPath;
    private String imageUrl;
    // Additional Image
    private List<ProductImage>productImages;
    // Inventory Response;
    private InventoryResponse inventoryResponse;

    private String color;
    // Brand
    private Brand brand;

    private String itemWebUrl;
    private String description;
    // Product Belongs to
    private ProductBelongsTo belongsTo;
    // Product Ratings
    public ProductRatingDto productRatingDto;
    @Data
    public static class Price{
        private String value;
        private String currency;
    }
    @Data
    public static class DiscountPrice{
        private Double value;
        private String currency;
        private Float percentage;
    }
    @Data
    public static class ProductImage{
        private String imageUrl;
        private ImageType imageType;

        private enum ImageType{
            POSTER,
            SLIDE,
            DESCRIPTION
        }
    }
    @Data
    public static class Brand{
        private String name;
    }
    @Data
    public static class ProductBelongsTo{
        private Long id;
        private int totalProducts;
    }
}
