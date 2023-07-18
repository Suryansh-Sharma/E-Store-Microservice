package com.suryansh.reviewservice.service;

import com.google.gson.Gson;
import com.suryansh.reviewservice.dto.ProductRatingDto;
import com.suryansh.reviewservice.entity.ProductRating;
import com.suryansh.reviewservice.exception.SpringReviewException;
import com.suryansh.reviewservice.model.RatingAndReviewModel;
import com.suryansh.reviewservice.repository.RatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class RatingServiceImpl implements RatingService {
    private static final Logger logger = LoggerFactory.getLogger(RatingServiceImpl.class);
    private final RatingRepository ratingRepository;
    private final Gson gson;

    public RatingServiceImpl(RatingRepository ratingRepository, Gson gson) {
        this.ratingRepository = ratingRepository;
        this.gson = gson;
    }

    @KafkaListener(topics = "add-new-product-rating")
    @Transactional
    public void saveNewProduct(String reviewRatingModelString) {
        RatingAndReviewModel ratingAndReviewModel = gson.fromJson(reviewRatingModelString,RatingAndReviewModel.class);

        var histogram = new ArrayList<ProductRating.ratings>();
        for (int i = 1; i <= 5; i++) {
            histogram.add(new ProductRating.ratings(Integer.toString(i), 0));
        }
        ProductRating productRating = ProductRating.builder()
                .productId(ratingAndReviewModel.productId())
                .productTitle(ratingAndReviewModel.productTitle())
                .averageRating(0)
                .ratingsHistogram(histogram).build();
        try {
            ratingRepository.save(productRating);
            logger.info("New Product is saved for rating of id {} ", productRating.getProductId());
        } catch (Exception e) {
            logger.error("Unable to save rating for product {} ", productRating.getProductId(), e);
            throw new SpringReviewException("Unable to save rating " + e);
        }
    }

    @Override
    public ProductRatingDto getRatingsForRatings(Long id) {
        var productRating = ratingRepository.findByProductId(id).orElseThrow(() -> new SpringReviewException("Unable to found product of id " + id));
        return new ProductRatingDto(productRating.getId(), id, productRating.getProductTitle(), productRating.getAverageRating(), productRating.getRatingsHistogram().stream().map(r -> new ProductRatingDto.Histogram(r.getValue(), r.getCount())).toList());
    }

    @Override
    @Transactional
    public void addNewRatingForProduct(Long productId, String userName, int rating) {
        ProductRating productRating = ratingRepository.findByProductId(productId).orElseThrow(() -> new SpringReviewException("Unable to find product of id: " + productId));

        float newAvgRating = 0;
        int totalRatingSum = 0;

        for (ProductRating.ratings r : productRating.getRatingsHistogram()) {
            if (rating == Integer.parseInt(r.getValue())) {
                r.setCount(r.getCount() + 1);
            }
            totalRatingSum += r.getCount();
            newAvgRating += Integer.parseInt(r.getValue()) * r.getCount();
        }

        if (totalRatingSum > 0) {
            newAvgRating /= totalRatingSum;
        }

        productRating.setAverageRating(newAvgRating);

        try {
            ratingRepository.save(productRating);
            logger.info("Product {} rating is successfully added by user {}", productId, userName);
        } catch (Exception e) {
            logger.error("Unable to add rating for product", e);
            throw new SpringReviewException("Unable to add rating for product");
        }
    }
    @Override
    @Transactional
    public void updateRatingForProduct(Long productId, String userName, int newRating, int oldRating) {
        ProductRating productRating = ratingRepository.findByProductId(productId)
                .orElseThrow(() -> new SpringReviewException("Unable to find product of id: " + productId));

        float newAvgRating = 0;
        int totalRatingSum = 0;

        for (ProductRating.ratings r : productRating.getRatingsHistogram()) {
            if (oldRating == Integer.parseInt(r.getValue())) {
                r.setCount(r.getCount() - 1);
            }
            if (newRating == Integer.parseInt(r.getValue())) {
                r.setCount(r.getCount() + 1);
            }
            totalRatingSum += r.getCount();
            newAvgRating += Integer.parseInt(r.getValue()) * r.getCount();
        }

        if (totalRatingSum > 0) {
            newAvgRating /= totalRatingSum;
        }

        productRating.setAverageRating(newAvgRating);

        try {
            ratingRepository.save(productRating);
            logger.info("Product {} rating is successfully updated by user {}", productId, userName);
        } catch (Exception e) {
            logger.error("Unable to update rating for product", e);
            throw new SpringReviewException("Unable to update rating for product");
        }
    }
    @Override
    @Transactional
    public void deleteRatingForProduct(Long productId, String userName, int rating) {
        ProductRating productRating = ratingRepository.findByProductId(productId)
                .orElseThrow(() -> new SpringReviewException("Unable to find product of id: " + productId));

        float newAvgRating = 0;
        int totalRatingSum = 0;

        for (ProductRating.ratings r : productRating.getRatingsHistogram()) {
            if (rating == Integer.parseInt(r.getValue())) {
                r.setCount(r.getCount() - 1);
            }
            totalRatingSum += r.getCount();
            newAvgRating += Integer.parseInt(r.getValue()) * r.getCount();
        }

        if (totalRatingSum > 0) {
            newAvgRating /= totalRatingSum;
        }

        productRating.setAverageRating(newAvgRating);

        try {
            ratingRepository.save(productRating);
            logger.info("Product {} rating is successfully deleted by user {}", productId, userName);
        } catch (Exception e) {
            logger.error("Unable to delete rating for product", e);
            throw new SpringReviewException("Unable to delete rating for product");
        }
    }


}
