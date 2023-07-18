package com.suryansh.reviewservice.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QuestionModel {
    @NotBlank(message = "Text can't be blank")
    private String text;
    @NotBlank(message = "Username can't be blank")
    private String username;
    @NotBlank(message = "Nickname can't be blank")
    private String nickname;
    @Min(value = 0,message = "Text can't be less than 0")
    private Long productId;
}
