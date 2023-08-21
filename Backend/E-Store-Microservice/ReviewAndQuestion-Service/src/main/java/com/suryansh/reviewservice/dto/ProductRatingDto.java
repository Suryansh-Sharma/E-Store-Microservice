package com.suryansh.reviewservice.dto;

import java.util.List;

public record ProductRatingDto(String id, Long productId, String productTitle, double averageRating , long ratingCount
        , List<Histogram> ratingsHistogram) {
    public record Histogram (double value,int count){}
}
