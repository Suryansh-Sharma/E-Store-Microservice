package com.suryansh.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class Seller {
    @Id
    private String id;
    private String username;
    private Long userId;
    private String feedbackPercentage;
    private String feedbackScore;
}
