package com.suryansh.model;

import com.suryansh.entity.ProductImage.ImageType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;


@Data
public class ProductModel {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;
    @Size(max = 50,message = "Short description can't be greater than 50")
    private String shortDescription;
    @Valid
    private Price price;
    @Valid
    private DiscountPrice discountPrice;
    @NotBlank(message = "Category tree can't be blank")
    private String categoryTree;
    @NotBlank(message = "Primary image url can't be blank")
    private String imageUrl;
    @Valid
    private List<ProductImage> additionalImages;
    private String color;
    @Valid
    private Brand brand;
    private String itemWebUrl;
    @NotBlank(message = "Description is required")
    private String description;
    private RichTextModel richText;

    // InventoryModel
    @Valid
    @NotNull
    private InventoryModel inventoryModel;
    // Filter Options model
    @Valid
    private List<ElasticSearchProductModel.Filter> filterOptions;
    // Rating and Review Model
    @Valid
    private RatingAndReview ratingAndReview;

    @Data
    public static class Price{
        @Min(value = 0, message = "Value must be greater than or equal to 0")
        private Double value;
        @NotBlank(message = "Currency is required")
        private String currency;
    }
    @Data
    public static class DiscountPrice{
        @Min(value = 0, message = "Value must be greater than or equal to 0")
        private Double value;
        @NotBlank(message = "Currency is required")
        private String currency;
        @Min(value = 0,message = "Discount can't be less than 0")
        @Max(value = 100,message = "Discount can't be greater than 100")
        private Float percentage;
    }
    @Data
    public static class ProductImage{
        @NotBlank(message = "ImageUrl is required")
        private String imageUrl;
        private ImageType imageType;

    }
    @Data
    public static class Brand{
        @NotBlank(message = "Brand is required")
        private String name;
    }
    @Data
    public static class RichTextModel{
        private String text1;
        private String text2;
    }
    @Data
    public static class RatingAndReview{
        @Min(value = 0,message = "Product rating can't be less than 0")
        @Max(value = 5,message = "Product rating can't be greater than 5")
        private Double rating;
        @Min(value = 0,message = "Product rating count can't be less than 0")
        private long ratingCount;
        @Min(value = 0,message = "Product review count can't be less than 0")
        private int reviewCount;
    }
}
