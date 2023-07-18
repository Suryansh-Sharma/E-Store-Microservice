package com.suryansh.reviewservice.service;

import com.suryansh.reviewservice.dto.PagingReviewDto;
import com.suryansh.reviewservice.dto.ReviewDto;
import com.suryansh.reviewservice.entity.Review;
import com.suryansh.reviewservice.exception.SpringReviewException;
import com.suryansh.reviewservice.model.ReviewModel;
import com.suryansh.reviewservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;
    private final RatingService ratingService;
    @Override
    @Transactional
    public void addReview(ReviewModel reviewModel) {
        Review review = Review.builder()
                .productId(reviewModel.getProductId())
                .noOfStars(reviewModel.getNoOfStars())
                .text(reviewModel.getText())
                .dateOfReview(Instant.now())
                .userName(reviewModel.getUserName())
                .build();
        try {
            // Add ratings for product.
            ratingService.addNewRatingForProduct(review.getProductId(),reviewModel.getUserName(),reviewModel.getNoOfStars());
            reviewRepository.save(review);
            log.info("Added review for user :  {} of productId : {}",reviewModel.getUserName(),reviewModel.getProductId());
        }catch (Exception e){
            log.error("Unable to add review for product of id {} by user {} ",reviewModel.getProductId(),reviewModel.getUserName());
            throw new SpringReviewException("Unable to save review ");
        }
    }
    @Override
    public PagingReviewDto getAllReviewForProduct(Long productId, Pageable pageable) {
        Page<Review>reviews =  reviewRepository.findByProductId(productId,pageable);
        List<ReviewDto> reviewDto = reviews.getContent().stream()
                .map(this::reviewEntityToDto)
                .toList();
        return PagingReviewDto.builder()
                .reviews(reviewDto)
                .totalPage(reviews.getTotalPages())
                .currentPage(pageable.getPageNumber()+1)
                .build();
    }

    @Override
    public List<ReviewDto> getAllReviewForUser(String userName) {
        return reviewRepository.findByUserName(userName).stream()
                .map(this::reviewEntityToDto)
                .toList();
    }
    @Override
    public void updateReview(ReviewModel reviewModel, String id) {
        Review review = reviewRepository.findById(id)
                        .orElseThrow(()->new SpringReviewException("No review found for update"));
        ratingService.updateRatingForProduct(reviewModel.getProductId(),reviewModel.getUserName()
                ,reviewModel.getNoOfStars(),review.getNoOfStars());
        review.setText(reviewModel.getText());
        review.setNoOfStars(reviewModel.getNoOfStars());
        review.setDateOfReview(Instant.now());
        try {
            reviewRepository.save(review);
            log.info("Review Updated Successfully for user {} ",reviewModel.getUserName());
        }catch (Exception e){
            log.error("Review can't be updated due to exception "+e);
            throw new SpringReviewException("Unable to update review ");
        }
    }
    @Override
    @Transactional
    public void deleteReview(String id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(()->new SpringReviewException("No review found for delete"));
        try {
            ratingService.deleteRatingForProduct(review.getProductId(),review.getUserName(),review.getNoOfStars());
            reviewRepository.deleteById(id);
            log.info("Review of id {} deleted successfully",id);
        }catch (Exception e){
            log.error("Unable to update review "+e);
            throw new SpringReviewException("Unable to delete review ");
        }
    }
    private ReviewDto reviewEntityToDto(Review review){
        PrettyTime prettyTime = new PrettyTime();
        return ReviewDto.builder()
                .id(review.getId())
                .productId(review.getProductId())
                .noOfStars(review.getNoOfStars())
                .text(review.getText())
                .userName(review.getUserName())
                .dateOfReview(prettyTime.format(review.getDateOfReview()))
                .build();
    }


}
