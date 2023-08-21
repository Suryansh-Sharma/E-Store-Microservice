package com.suryansh.dto;

import java.util.List;

public record ProductPagingDto(int current_page
        , int total_pages
        , List<ProductDto>products
        , long total_data) {}
