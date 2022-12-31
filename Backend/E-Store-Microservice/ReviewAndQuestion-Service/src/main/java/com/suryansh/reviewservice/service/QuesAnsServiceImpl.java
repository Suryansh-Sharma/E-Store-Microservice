package com.suryansh.reviewservice.service;

import com.suryansh.reviewservice.dto.AnswerDto;
import com.suryansh.reviewservice.dto.PagingQuestionDto;
import com.suryansh.reviewservice.dto.QuestionDto;
import com.suryansh.reviewservice.entity.Answer;
import com.suryansh.reviewservice.entity.Question;
import com.suryansh.reviewservice.exception.SpringReviewException;
import com.suryansh.reviewservice.model.AnswerModel;
import com.suryansh.reviewservice.model.QuestionModel;
import com.suryansh.reviewservice.repository.AnswerRepository;
import com.suryansh.reviewservice.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuesAnsServiceImpl implements QuesAnsService{
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    @Override
    @Transactional
    public String addQuestion(QuestionModel questionModel) {
        Question question=Question.builder()
                .text(questionModel.getText())
                .username(questionModel.getUsername())
                .date(Instant.now())
                .productId(questionModel.getProductId())
                .noOfAnswers(0)
                .build();
        try {
            questionRepository.save(question);
            return "Question Added Successfully for User : "+questionModel.getUsername()+
                    " for product : "+questionModel.getProductId();
        }catch (Exception e){
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
        Answer answer = Answer.builder()
                .questionId(answerModel.getQuestionId())
                .username(answerModel.getUsername())
                .text(answerModel.getText())
                .date(Instant.now())
                .build();
        question.setNoOfAnswers(question.getNoOfAnswers()+1);
        try {
            answerRepository.save(answer);
            questionRepository.save(question);
            return "Answer Added Successfully for User : "+answerModel.getUsername()+
                    " for Question : "+answerModel.getQuestionId();
        }catch (Exception e){
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
                .currentPage(pageable.getPageNumber())
                .build();
    }

    @Override
    public List<AnswerDto> getAnswerByQuestionId(String id) {
        Optional<Question> question = questionRepository.findById(id);
        if (question.isEmpty())return null;
        if (question.get().getNoOfAnswers()==0)return null;
        return answerRepository.findByQuestionId(id).stream()
                .map(this::answerEntityToDto)
                .toList();
    }

    private AnswerDto answerEntityToDto(Answer answer) {
        PrettyTime prettyTime = new PrettyTime();
        return AnswerDto.builder()
                .id(answer.getId())
                .questionId(answer.getQuestionId())
                .text(answer.getText())
                .username(answer.getUsername())
                .date(prettyTime.format(answer.getDate()))
                .build();
    }

    private QuestionDto QuestionEntityToDto(Question question) {
        PrettyTime prettyTime = new PrettyTime();
        return QuestionDto.builder()
                .id(question.getId())
                .productId(question.getProductId())
                .text(question.getText())
                .username(question.getUsername())
                .noOfAnswers(question.getNoOfAnswers())
                .date(prettyTime.format(question.getDate()))
                .build();
    }
}
