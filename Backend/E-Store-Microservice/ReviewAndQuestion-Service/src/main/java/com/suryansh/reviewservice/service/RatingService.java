package com.suryansh.reviewservice.service;

import com.suryansh.reviewservice.dto.ProductRatingDto;

public interface RatingService {

    ProductRatingDto getRatingsForRatings(Long id);

    void addNewRatingForProduct(Long productId, String userName,double rating);

    void updateRatingForProduct(Long productId, String userName, double noOfStars, double noOfStars1);

    void deleteRatingForProduct(Long productId, String userName, double noOfStars);
}
