package com.suryansh.reviewservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Builder
public class PagingQuestionDto {
    private List<QuestionDto>questions;
    private int totalPage;
    private int currentPage;
}
