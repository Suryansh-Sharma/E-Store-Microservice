package com.suryansh.reviewservice.service;

import com.suryansh.reviewservice.dto.ProductReviewsDto;
import com.suryansh.reviewservice.entity.Review;
import com.suryansh.reviewservice.exception.SpringReviewException;
import com.suryansh.reviewservice.model.ReviewModel;
import com.suryansh.reviewservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;
    private final RatingService ratingService;
    @Override
    @Transactional
    public void addReview(ReviewModel reviewModel) {
        Review review = reviewRepository.getReviewByProductId(reviewModel.getProductId());
        Review.AllReviews newReview = Review.AllReviews.builder()
                .userName(reviewModel.getUserName())
                .nickname(reviewModel.getNickName())
                .stars(reviewModel.getNoOfStars())
                .text(reviewModel.getText())
                .dateOfReview(Instant.now())
                .build();
        int count = review.getReviewCount();
        List<Review.AllReviews> allReview = review.getAllReviews();
        allReview.add(newReview);
        review.setProductId(review.getProductId());
        review.setAllReviews(allReview);
        if (count > 0) {
            review.setReviewCount(count + 1);
        } else {
            review.setReviewCount(1);
        }
        try {
            // Add ratings for product.
            ratingService.addNewRatingForProduct(review.getProductId(), reviewModel.getUserName(), reviewModel.getNoOfStars());
            reviewRepository.save(review);
            log.info("Added review for user :  {} of productId : {}", reviewModel.getUserName(), reviewModel.getProductId());
        } catch (Exception e) {
            log.error("Unable to add review for product of id {} by user {} ", reviewModel.getProductId(), reviewModel.getUserName());
            throw new SpringReviewException("Unable to save review ");
        }
    }

    @Override
    public ProductReviewsDto getAllReviewForProduct(Long productId, Pageable pageable) {
        Optional<Review> review = reviewRepository.findByProductId(productId);
        if (review.isEmpty()){
            throw new SpringReviewException("Unable to find review of product_id "+productId);
        }

        int skip = (pageable.getPageNumber() - 1) * pageable.getPageSize();
        List<Review.AllReviews> paginatedReviews = reviewRepository.getPaginatedReviewsForProduct(productId, skip, pageable.getPageSize());

        int totalPages = (int) Math.ceil((double) review.get().getAllReviews().size() / pageable.getPageSize());

        return new ProductReviewsDto(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                review.get().getId(),
                productId,
                paginatedReviews,
                review.get().getAllReviews().size(),
                totalPages
        );
    }

    @Override
    public List<ProductReviewsDto> getAllReviewForUser(String userName) {
        return null;
    }

    @Override
    public void updateReview(ReviewModel reviewModel, String id, Instant dateOfReview) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new SpringReviewException("No review found for update"));
        Optional<Review.AllReviews> foundReviewOptional = review.getAllReviews().stream()
                .filter(r -> r.getDateOfReview().equals(dateOfReview))
                .findFirst();
        if (foundReviewOptional.isEmpty()) {
            throw new SpringReviewException("Unable to find review for particular date-and-time " + dateOfReview);
        }
        Review.AllReviews foundReview = foundReviewOptional.get();

        ratingService.updateRatingForProduct(reviewModel.getProductId(), reviewModel.getUserName()
                , reviewModel.getNoOfStars(), foundReview.getStars());

        foundReview.setText(reviewModel.getText());
        foundReview.setStars(reviewModel.getNoOfStars());

        try {
            reviewRepository.save(review);
            log.info("Review Updated Successfully for user {} ", reviewModel.getUserName());
        } catch (Exception e) {
            log.error("Review can't be updated due to exception " + e);
            throw new SpringReviewException("Unable to update review ");
        }
    }

    @Override
    @Transactional
    public void deleteReview(String id, Instant dateOfReview) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new SpringReviewException("No review found for delete"));
        Optional<Review.AllReviews> foundOptionalReview = review.getAllReviews().stream()
                .filter(r -> r.getDateOfReview().equals(dateOfReview))
                .findFirst();
        if (foundOptionalReview.isEmpty()) {
            throw new SpringReviewException("Unable to find review for particular date-and-time " + dateOfReview);
        }
        Review.AllReviews foundReview = foundOptionalReview.get();
        review.getAllReviews().remove(foundReview);
        try {
            ratingService.deleteRatingForProduct(review.getProductId(), foundReview.getUserName(), foundReview.getStars());
            reviewRepository.save(review);
            log.info("Review of user {} at date {} is deleted successfully", foundReview.getUserName(), dateOfReview);
        } catch (Exception e) {
            log.error("Unable to update review " + e);
            throw new SpringReviewException("Unable to delete review ");
        }
    }


}
