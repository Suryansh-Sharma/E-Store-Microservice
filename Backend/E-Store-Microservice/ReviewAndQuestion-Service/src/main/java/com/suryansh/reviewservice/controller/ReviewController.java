package com.suryansh.reviewservice.controller;

import com.suryansh.reviewservice.dto.PagingReviewDto;
import com.suryansh.reviewservice.dto.ReviewDto;
import com.suryansh.reviewservice.model.ReviewModel;
import com.suryansh.reviewservice.service.ReviewService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

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
    public PagingReviewDto getAllReviewByProductId(@PathVariable Long productId,
                                                         @RequestParam(value = "pageNo",required = false
                                                                 ,defaultValue = "0")int page){
        Pageable pageable = PageRequest.of(page,6);
        return reviewService.getAllReviewForProduct(productId,pageable);
    }
    @GetMapping("/by-username/{userName}")
    public List<ReviewDto> getReviewByUser(@PathVariable String userName){
        return reviewService.getAllReviewForUser(userName);
    }
    @PutMapping("/update/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public void updateReview(@RequestBody @Valid ReviewModel reviewModel, @PathVariable String id){
        reviewService.updateReview(reviewModel,id);
    }
    @DeleteMapping("/delete/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public void deleteReview(@PathVariable String id){
        reviewService.deleteReview(id);
    } 
}
