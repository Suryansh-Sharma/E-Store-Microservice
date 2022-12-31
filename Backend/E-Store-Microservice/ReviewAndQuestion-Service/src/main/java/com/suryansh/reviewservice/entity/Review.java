package com.suryansh.reviewservice.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
@Getter
@Setter
@Builder
@Document(collection = "review")
public class Review {
    @Id
    private String id;
    private Long productId;
    private int noOfStars;
    private String text;
    private Instant dateOfReview;
    private String userName;

}
