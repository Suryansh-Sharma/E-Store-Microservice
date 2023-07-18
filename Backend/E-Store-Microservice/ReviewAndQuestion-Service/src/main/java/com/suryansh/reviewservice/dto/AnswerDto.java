package com.suryansh.reviewservice.dto;

import java.time.Instant;


public record AnswerDto(String text,String username,String nickname,Instant date) {}
