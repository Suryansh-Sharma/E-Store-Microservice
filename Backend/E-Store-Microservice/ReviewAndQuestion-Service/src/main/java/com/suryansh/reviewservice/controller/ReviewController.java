package com.suryansh.reviewservice.controller;

import com.suryansh.reviewservice.dto.ProductReviewsDto;
import com.suryansh.reviewservice.model.ReviewModel;
import com.suryansh.reviewservice.service.ReviewService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/review")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    @PostMapping("/add")
    @Async
    @SecurityRequirement(name = "bearerAuth")
    public void addReview(@RequestBody @Valid ReviewModel reviewModel){
        reviewService.addReview(reviewModel);
    }
    @GetMapping("/by-productId/{productId}")
    public ProductReviewsDto getAllReviewByProductId(@PathVariable Long productId
            ,@RequestParam(value = "page_no",required = false,defaultValue = "0")int page_no
            ,@RequestParam(value = "page_size",required = false,defaultValue = "6")int page_size){
        Pageable pageable = PageRequest.of(page_no,page_size);
        return reviewService.getAllReviewForProduct(productId,pageable);
    }
    @GetMapping("/by-username/{userName}")
    public ProductReviewsDto getReviewByUser(@PathVariable String userName){
//        return reviewService.getAllReviewForUser(userName);
        return null;
    }
    @PutMapping("/update/{id}/{dateOfReview}")
    @SecurityRequirement(name = "bearerAuth")
    public void updateReview(@RequestBody @Valid ReviewModel reviewModel, @PathVariable String id,@PathVariable Instant dateOfReview){
        reviewService.updateReview(reviewModel,id,dateOfReview);
    }
    @DeleteMapping("/delete/{id}/{dateOfReview}")
    @SecurityRequirement(name = "bearerAuth")
    public void deleteReview(@PathVariable String id,@PathVariable Instant dateOfReview){
        reviewService.deleteReview(id,dateOfReview);
    } 
}
