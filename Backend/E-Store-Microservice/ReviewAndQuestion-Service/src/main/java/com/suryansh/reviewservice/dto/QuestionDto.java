package com.suryansh.reviewservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Builder
public class QuestionDto {
    private String id;
    private Long productId;
    private String text;
    private String username;
    private String date;
    private int noOfAnswers;
    private String nickname;

}
