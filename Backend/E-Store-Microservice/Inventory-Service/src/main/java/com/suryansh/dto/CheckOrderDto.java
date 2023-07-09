package com.suryansh.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CheckOrderResponse {
    private boolean isSomeThingOutOfStock;
    private int totalProducts;
    private ArrayList<ProductAvailability>productAvailabilities;
    @Data
    public static class ProductAvailability{
        private Long productId;
        private String title;
        private int availableAmount;
    }
}
