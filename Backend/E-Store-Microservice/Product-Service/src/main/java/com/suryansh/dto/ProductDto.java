package com.suryansh.dto;

public record ProductDto(Long id, String title, String shortDescription,
                         Price price, String categoryTree, String imageUrl, String color,
                         String itemWebUrl, String description) {

    public record Price(double value, String currency) {}
}
