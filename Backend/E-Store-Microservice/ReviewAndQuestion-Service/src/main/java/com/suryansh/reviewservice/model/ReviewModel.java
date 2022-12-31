package com.suryansh.reviewservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewModel {
    private String id;
    private Long productId;
    private int noOfStars;
    private String text;
    private String userName;

}
