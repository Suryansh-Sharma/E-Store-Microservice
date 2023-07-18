package com.suryansh.model;

import com.suryansh.entity.ProductImage.ImageType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;


@Data
public class ProductModel {
    private Long id;

    private BelongsTo productBelongsTo;

    @NotBlank(message = "Title is required")
    private String title;

    // InventoryModel
    @Valid
    private InventoryModel inventoryModel;

    @NotBlank(message = "SubTitle is required")
    private String subTitle;

    @NotBlank(message = "Short description is required")
    private String shortDescription;

    @Valid
    private Price price;

    private String categoryPath;
    private String imageUrl;

    @Valid
    private List<ProductImage> productImages;

    private String color;

    @Valid
    private Brand brand;

    private String itemWebUrl;

    @NotBlank(message = "Description is required")
    private String description;

    @Valid
    private String belongsTo;

    @Valid
    private List<ProductElasticSearchModel.Filter> filterModel;

    @Data
    public static class Price{
        @NotBlank(message = "Price is required")
        private String value;
        @NotBlank(message = "Currency is required")
        private String currency;
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
    public static class BelongsTo{
        private Long id;
        private boolean haveParent;
        private String productName;
    }
}
