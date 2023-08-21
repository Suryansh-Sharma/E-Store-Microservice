package com.suryansh.reviewservice.dto;

import com.suryansh.reviewservice.entity.Review;

import java.util.List;

public record ProductReviewsDto(int page_no, int page_size, String review_id, long product_id, List<Review.AllReviews>all_reviews, int review_count,
                                int total_pages){
}
