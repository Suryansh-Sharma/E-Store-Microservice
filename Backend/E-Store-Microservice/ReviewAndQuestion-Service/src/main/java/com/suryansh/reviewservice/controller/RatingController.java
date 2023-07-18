package com.suryansh.reviewservice.controller;

import com.suryansh.reviewservice.dto.ProductRatingDto;
import com.suryansh.reviewservice.service.RatingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/rating/")
@CrossOrigin("*")
public class RatingController {
    private final RatingService ratingService;

    RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }


    @GetMapping("product-id/{id}")
    public ProductRatingDto getProductRatings(@PathVariable Long id) {
        return ratingService.getRatingsForRatings(id);
    }
}
