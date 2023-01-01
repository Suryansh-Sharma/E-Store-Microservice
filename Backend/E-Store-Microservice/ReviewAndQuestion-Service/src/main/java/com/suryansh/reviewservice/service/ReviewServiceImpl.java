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
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;
    private final WebClient.Builder webClientBuilder;
    @Override
    @Transactional
    public void addReview(ReviewModel reviewModel, String token) {
        Review review = Review.builder()
                .productId(reviewModel.getProductId())
                .noOfStars(reviewModel.getNoOfStars())
                .text(reviewModel.getText())
                .dateOfReview(Instant.now())
                .userName(reviewModel.getUserName())
                .build();
        try {
            // Calling Product Service for updating rating inside product.
            String productResponse = webClientBuilder.build().post()
                    .uri("http://localhost:8080/api/products/addRatingForProduct/"
                            + reviewModel.getProductId() + "/" + reviewModel.getNoOfStars())
                    .header("Authorization",token)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            reviewRepository.save(review);
            log.info(productResponse);
            log.info("Added review for user :  {} of productId : {}",reviewModel.getUserName(),reviewModel.getProductId());
        }catch (Exception e){
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
                .currentPage(pageable.getPageNumber())
                .build();
    }

    @Override
    public List<ReviewDto> getAllReviewForUser(String userName) {
        return reviewRepository.findByUserName(userName).stream()
                .map(this::reviewEntityToDto)
                .toList();
    }
    @Override
    public void updateReview(ReviewModel reviewModel) {
        Review review = reviewRepository.findById(reviewModel.getId())
                        .orElseThrow(()->new SpringReviewException("No review found for update"));
        System.out.println(review);
        review.setText(reviewModel.getText());
        review.setNoOfStars(reviewModel.getNoOfStars());
        review.setDateOfReview(Instant.now());
        reviewRepository.save(review);
        log.info("Review Updated Successfully for user {} ",reviewModel.getUserName());
    }
    @Override
    @Transactional
    public void deleteReview(String id) {
        reviewRepository.deleteById(id);
        log.info("Review of id {} deleted successfully",id);
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
