package com.suryansh.reviewservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document("ProductRating")
@Data
@Builder
public class ProductRating {
    @Id
    private String id;
    private Long productId;
    private String productTitle;
    private float averageRating;
    ArrayList<ratings>ratingsHistogram;
    @Data
    @AllArgsConstructor
    public static class ratings{
        private String value;
        private int count;
    }
}
