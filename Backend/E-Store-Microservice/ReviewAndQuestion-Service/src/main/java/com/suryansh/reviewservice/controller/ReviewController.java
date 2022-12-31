package com.suryansh.reviewservice.controller;

import com.suryansh.reviewservice.dto.PagingReviewDto;
import com.suryansh.reviewservice.dto.ReviewDto;
import com.suryansh.reviewservice.model.ReviewModel;
import com.suryansh.reviewservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/review")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    @PostMapping("/addReview")
    @Async
    public void addReview(@RequestBody ReviewModel reviewModel,
                          @RequestHeader(name = "Authorization") String token){
        reviewService.addReview(reviewModel,token);
    }
    @GetMapping("/getReviewsByProductId/{productId}")
    public PagingReviewDto getAllReviewByProductId(@PathVariable Long productId,
                                                         @RequestParam(value = "pageNo",required = false
                                                                 ,defaultValue = "0")int page){
        Pageable pageable = PageRequest.of(page,6);
        return reviewService.getAllReviewForProduct(productId,pageable);
    }
    @GetMapping("/getReviewsByUserName/{userName}")
    public List<ReviewDto> getReviewByUser(@PathVariable String userName){
        return reviewService.getAllReviewForUser(userName);
    }
    @PutMapping("/updateReview")
    public void updateReview(@RequestBody ReviewModel reviewModel){
        reviewService.updateReview(reviewModel);
    }
    @DeleteMapping("/deleteReview/{id}")
    public void deleteReview(@PathVariable String id){
        reviewService.deleteReview(id);
    } 
}
