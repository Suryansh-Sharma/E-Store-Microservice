package com.suryansh.dto;

public record ProductDto(Long id, String title, String subTitle, String shortDescription,
                         Price price, String categoryPath, String imageUrl, String color,
                         String itemWebUrl, String description) {

    public record Price(String value, String currency) {}
}
