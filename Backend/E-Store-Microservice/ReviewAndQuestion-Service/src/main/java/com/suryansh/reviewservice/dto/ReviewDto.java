package com.suryansh.reviewservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewDto{
    private String id;
    private Long productId;
    private int noOfStars;
    private String text;
    private String dateOfReview;
    private String userName;
    private String nickname;

}
