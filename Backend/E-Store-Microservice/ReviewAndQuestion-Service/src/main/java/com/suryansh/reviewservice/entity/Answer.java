package com.suryansh.reviewservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class Answer {
    private String text;
    private String username;
    private String nickname;
    private Instant date;
}
