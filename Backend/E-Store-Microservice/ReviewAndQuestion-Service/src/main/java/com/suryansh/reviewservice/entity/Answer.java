package com.suryansh.reviewservice.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "answers")
@Getter
@Setter
@Builder
public class Answer {
    private String id;
    private String questionId;
    private String text;
    private String username;
    private String nickname;
    private Instant date;
}
