package com.suryansh.reviewservice.service;

import com.suryansh.reviewservice.dto.ProductReviewsDto;
import com.suryansh.reviewservice.model.ReviewModel;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

public interface ReviewService {
    void addReview(ReviewModel reviewModel);

    ProductReviewsDto getAllReviewForProduct(Long productId, Pageable pageable);

    List<ProductReviewsDto> getAllReviewForUser(String userName);

    void updateReview(ReviewModel reviewModel, String id, Instant dateOfReview);

    void deleteReview(String id, Instant dateOfReview);
}
