package com.suryansh.reviewservice.service;

import com.suryansh.reviewservice.dto.PagingReviewDto;
import com.suryansh.reviewservice.dto.ReviewDto;
import com.suryansh.reviewservice.model.ReviewModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    void addReview(ReviewModel reviewModel);

    PagingReviewDto getAllReviewForProduct(Long productId, Pageable pageable);

    List<ReviewDto> getAllReviewForUser(String userName);

    void updateReview(ReviewModel reviewModel, String id);

    void deleteReview(String id);
}
