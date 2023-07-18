package com.suryansh.reviewservice.service;

import com.suryansh.reviewservice.dto.ProductRatingDto;

public interface RatingService {
    void saveNewProduct(Long productId, String title);

    ProductRatingDto getRatingsForRatings(Long id);

    void addNewRatingForProduct(Long productId, String userName,int rating);

    void updateRatingForProduct(Long productId, String userName, int noOfStars, int noOfStars1);

    void deleteRatingForProduct(Long productId, String userName, int noOfStars);
}
