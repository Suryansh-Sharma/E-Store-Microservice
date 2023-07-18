package com.suryansh.reviewservice.dto;

import java.util.List;

public record ProductRatingDto(String id, Long productId, String productTitle, float averageRating
        , List<Histogram> ratingsHistogram) {
    public record Histogram (String value,int count){}
}
