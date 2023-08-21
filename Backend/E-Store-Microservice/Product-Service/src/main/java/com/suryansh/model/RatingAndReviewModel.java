package com.suryansh.model;

public record RatingAndReviewModel(Long productId, String productTitle
        ,Double averageRating,long ratingCount,int reviewCount) {
}
