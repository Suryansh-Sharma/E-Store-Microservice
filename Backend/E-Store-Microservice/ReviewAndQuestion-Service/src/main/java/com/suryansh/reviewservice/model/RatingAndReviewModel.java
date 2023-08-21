package com.suryansh.reviewservice.model;

public record RatingAndReviewModel(Long productId, String productTitle
,Double averageRating,long ratingCount,int reviewCount) {
}
