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

    @PostMapping("new-product/{productId}/{title}")
    public void addNewProductForRating(@PathVariable Long productId, @PathVariable String title) {
        ratingService.saveNewProduct(productId, title);
    }

    @GetMapping("product-id/{id}")
    public ProductRatingDto getProductRatings(@PathVariable Long id) {
        return ratingService.getRatingsForRatings(id);
    }
}
