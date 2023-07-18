package com.suryansh.model;

import java.util.List;

public record CheckOrderModel(int totalProducts, List<Product> products) {

    public record Product(Long productId, int requiredAmount) {
    }
}