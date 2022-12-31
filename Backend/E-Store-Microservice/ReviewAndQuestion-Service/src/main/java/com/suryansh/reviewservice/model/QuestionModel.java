package com.suryansh.reviewservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuestionModel {
    private String text;
    private String username;
    private Long productId;
}
