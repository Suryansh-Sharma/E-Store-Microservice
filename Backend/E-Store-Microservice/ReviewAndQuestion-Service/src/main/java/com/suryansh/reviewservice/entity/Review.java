package com.suryansh.reviewservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@Document(collection = "review")
public class Review {
    @Id
    private String id;
    private Long productId;
    private int reviewCount;
    private List<AllReviews> allReviews;


    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AllReviews{
        private String userName;
        private String nickname;
        private double stars;
        private String text;
        private Instant dateOfReview;

        @Override
        public boolean equals(Object o){
            if (this == o)return true;
            if (o == null || getClass() != o.getClass())return false;
            AllReviews that = (AllReviews) o;
            return Objects.equals(dateOfReview,that.dateOfReview);
        }
        @Override
        public int hashCode() {
            return Objects.hash(getDateOfReview());
        }
    }
}
