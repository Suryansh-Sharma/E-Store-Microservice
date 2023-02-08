package com.suryansh.reviewservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnswerDto {
    private String id;
    private String questionId;
    private String text;
    private String username;
    private String date;
    private String nickname;

}
