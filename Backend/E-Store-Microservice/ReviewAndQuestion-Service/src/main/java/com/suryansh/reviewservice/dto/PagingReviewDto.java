package com.suryansh.reviewservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Builder
@Getter
@Setter
public class PagingReviewDto {
    private List<ProductReviewsDto> reviews;
    private int currentPage;
    private int totalPage;
}
