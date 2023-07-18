package com.suryansh.reviewservice.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;

@Document(collection = "Question&Answers")
@Builder
@Data
public class Question {
    private String id;
    private Long productId;
    private String text;
    private String username;
    private String nickname;
    private Instant date;
    private int noOfAnswers;
    private ArrayList<Answer>answers;
}

