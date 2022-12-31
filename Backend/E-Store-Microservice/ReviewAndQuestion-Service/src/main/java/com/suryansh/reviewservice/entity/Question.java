package com.suryansh.reviewservice.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "question")
@Getter
@Setter
@Builder
public class Question {
    private String id;
    private Long productId;
    private String text;
    private String username;
    private Instant date;
    private int noOfAnswers;

}
