package com.suryansh.reviewservice.service;

import com.suryansh.reviewservice.dto.AnswerDto;
import com.suryansh.reviewservice.dto.PagingQuestionDto;
import com.suryansh.reviewservice.dto.QuestionDto;
import com.suryansh.reviewservice.entity.Answer;
import com.suryansh.reviewservice.entity.Question;
import com.suryansh.reviewservice.exception.SpringReviewException;
import com.suryansh.reviewservice.model.AnswerModel;
import com.suryansh.reviewservice.model.QuestionModel;
import com.suryansh.reviewservice.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.ocpsoft.prettytime.PrettyTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuesAnsServiceImpl implements QuesAnsService{
    private final QuestionRepository questionRepository;
    private static final Logger logger = LoggerFactory.getLogger(QuesAnsServiceImpl.class);
    @Override
    @Transactional
    public String addQuestion(QuestionModel questionModel) {
        Question question=Question.builder()
                .text(questionModel.getText())
                .username(questionModel.getUsername())
                .nickname(questionModel.getNickname())
                .date(Instant.now())
                .productId(questionModel.getProductId())
                .noOfAnswers(0)
                .answers(null)
                .build();
        try {
            questionRepository.save(question);
            logger.info("New Question is added by user {} for product {} ",question.getUsername(),question.getProductId());
            return "Question Added Successfully for User : "+questionModel.getUsername()+
                    " for product : "+questionModel.getProductId();
        }catch (Exception e){
            logger.error("Unable to add new question "+e);
            return "Sorry Unable to Add Question for User : "+questionModel.getUsername()+
                    " for product : "+questionModel.getProductId();
        }
    }

    @Override
    @Transactional
    public String addAnswer(AnswerModel answerModel) {
        Question question = questionRepository.findById(answerModel.getQuestionId())
                .orElseThrow(()->new SpringReviewException("Unable to find question for id "
                        +answerModel.getQuestionId()));
        Answer answer = new Answer(answerModel.getText(),answerModel.getUsername(),answerModel.getNickname(),Instant.now());
        question.setNoOfAnswers(question.getNoOfAnswers()+1);
        question.getAnswers().add(answer);
        try {
            questionRepository.save(question);
            return "Answer Added Successfully for User : "+answerModel.getUsername()+
                    " for Question : "+answerModel.getQuestionId();
        }catch (Exception e){
            logger.error("Unable to add new Answer for question {} ",question.getId()+e);
            return "Unable to Add Answer for User : "+answerModel.getUsername()+
                    " for Question : "+answerModel.getQuestionId();
        }
    }

    @Override
    public PagingQuestionDto getByProductId(Long productId, Pageable pageable) {
        Page<Question>questionPage = questionRepository.findByProductId(productId,pageable);
        List<QuestionDto>questions = questionPage.getContent().stream()
                .map(this::QuestionEntityToDto)
                .toList();
        return PagingQuestionDto.builder()
                .questions(questions)
                .totalPage(questionPage.getTotalPages())
                .currentPage(pageable.getPageNumber()+1)
                .totalData(questionPage.getTotalElements())
                .build();
    }

    @Override
    @Transactional
    public String updateQuestion(QuestionModel model, String quesId) {
        Question question = questionRepository.findById(quesId)
                .orElseThrow(()->new SpringReviewException("Unable to find question of id "+quesId));
        question.setText(model.getText());
        question.setDate(Instant.now());
        try {
            questionRepository.save(question);
            logger.info("Question {} is updated by user {} ",question.getText(),question.getUsername());
            return "Question is successfully updated by user ";
        }catch (Exception e){
            logger.error("Unable to update question {} by user {} ",question.getText(),question.getUsername()+e);
            throw new SpringReviewException("Unable to update question ");
        }
    }


    private QuestionDto QuestionEntityToDto(Question question) {
        PrettyTime prettyTime = new PrettyTime();
        List<AnswerDto>answers=question.getAnswers()
                .stream()
                .map(a->new AnswerDto(a.getText(),a.getUsername(),a.getNickname(),a.getDate()))
                .toList();
        return QuestionDto.builder()
                .id(question.getId())
                .productId(question.getProductId())
                .text(question.getText())
                .username(question.getUsername())
                .nickname(question.getNickname())
                .noOfAnswers(question.getNoOfAnswers())
                .date(prettyTime.format(question.getDate()))
                .answers(answers)
                .build();
    }
}
