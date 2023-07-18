package com.suryansh.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ProductElasticSearchModel {
    private int id;
    private String name;
    private String brand;
    private List<String> productCategoryTree;
    private String description;
    private int quantity;
    private double price;
    private String productRating;
    @Valid
    private List<Filter> filters;

    @Data
    public static class Filter{
        @NotBlank(message = "Filter Name can't be blank")
        private String filterName;
        @NotBlank(message = "Filter Value can't be blank")
        private String filterValue;
    }
}
