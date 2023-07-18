package com.suryansh.reviewservice.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewModel {
    @Min(value = 0,message = "Product id can't be less than 0")
    private Long productId;
    @Min(value = 1,message = "No of stars can't be less than 0")
    @Min(value = 5,message = "No of stars can't be greater than 5")
    private int noOfStars;
    @NotBlank(message = "Text can't be blank")
    private String text;
    @NotBlank(message = "Username can't be blank")
    private String userName;

}
