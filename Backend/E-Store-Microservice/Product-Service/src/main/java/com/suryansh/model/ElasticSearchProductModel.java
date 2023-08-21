package com.suryansh.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ElasticSearchProductModel {
    @NotBlank(message = "Product id can't be blank")
    private String id;
    @NotBlank(message = "Product name can't be blank")
    private String name;
    @NotBlank(message = "Product image can't be blank")
    private String image;
    @NotBlank(message = "Product brand name can't be blank")
    private String brand;
    @Min(value = 0,message = "Product rating can't be less than 0")
    @Max(value = 5,message = "Product rating can't be greater than 5")
    private Double rating;
    @Min(value = 0,message = "Product rating count can't be less than 0")
    private long ratingCount;
    @Min(value = 0,message = "Product review count can't be less than 0")
    private int reviewCount;
    @Valid
    private Price price;
    @Valid
    private Price newPrice;
    @Min(value = 0,message = "Discount can't be less than 0")
    @Max(value = 100,message = "Discount can't be greater than 100")
    private float discount;
    @NotBlank(message = "Product category can't be blank")
    private String categoryTree;
    @Size(max = 50,message = "Short description can't be greater than length of 30")
    private String shortDescription;

    @Valid
    private List<Filter> filters;

    @Data
    @AllArgsConstructor
    public static class Price {
        @NotBlank(message = "Product price currency can't be blank")
        private String currency;
        @NotNull(message = "Product price value can't be blank")
        private Double value;
    }

    @Data
    public static class Filter {
        @NotBlank(message = "Product filter key can't be blank")
        private String key;
        @NotBlank(message = "Product value key can't be blank")
        private String value;
    }
}
