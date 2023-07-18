package com.suryansh.dto;


import java.util.List;

public record CheckOrderDto(boolean isSomeThingOutOfStock, int totalProducts, List<Product> products) {

    public record Product(Long productId, String title,boolean isInStock, int availableAmount) {
    }
}

