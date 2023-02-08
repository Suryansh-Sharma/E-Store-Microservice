package com.suryansh.reviewservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerModel {
    private String questionId;
    private String text;
    private String username;
    private String nickname;

}
