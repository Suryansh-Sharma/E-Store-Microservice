package com.suryansh.reviewservice.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnswerModel {
    @NotBlank(message = "Question Id can't be blank")
    private String questionId;
    @NotBlank(message = "Text can't be blank")
    private String text;
    @NotBlank(message = "Username can't be blank")
    private String username;
    @NotBlank(message = "Nickname can't be blank")
    private String nickname;

}
