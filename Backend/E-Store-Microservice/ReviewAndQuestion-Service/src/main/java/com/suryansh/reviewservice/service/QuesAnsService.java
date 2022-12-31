package com.suryansh.reviewservice.service;

import com.suryansh.reviewservice.dto.AnswerDto;
import com.suryansh.reviewservice.dto.PagingQuestionDto;
import com.suryansh.reviewservice.model.AnswerModel;
import com.suryansh.reviewservice.model.QuestionModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuesAnsService {
    String addQuestion(QuestionModel questionModel);

    String addAnswer(AnswerModel answerModel);

    PagingQuestionDto getByProductId(Long productId, Pageable pageable);

    List<AnswerDto> getAnswerByQuestionId(String id);
}
