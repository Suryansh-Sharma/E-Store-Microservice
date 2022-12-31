package com.suryansh.reviewservice.controller;

import com.suryansh.reviewservice.dto.AnswerDto;
import com.suryansh.reviewservice.dto.PagingQuestionDto;
import com.suryansh.reviewservice.model.AnswerModel;
import com.suryansh.reviewservice.model.QuestionModel;
import com.suryansh.reviewservice.service.QuesAnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/QuestionAndAnswer")
@CrossOrigin("*")
@RequiredArgsConstructor
public class QuestionAnswerController {
    private final QuesAnsService quesAnsService;
    @PostMapping("/addNewQuestion")
    public String addNewQuestion(@RequestBody QuestionModel questionModel){
        return quesAnsService.addQuestion(questionModel);
    }
    @PostMapping("/addAnswer")
    public String addAnswer(@RequestBody AnswerModel answerModel){
        return quesAnsService.addAnswer(answerModel);
    }
    @GetMapping("/getByProductId/{productId}")
    public PagingQuestionDto getQuestionByProductId(@PathVariable Long productId,
                                                    @RequestParam(name = "pageNo",defaultValue = "0",required = false)
                                                    int pageNo){
        Pageable pageable = PageRequest.of(pageNo,6);
        return quesAnsService.getByProductId(productId,pageable);
    }
    @GetMapping("/getAnswerByQuestionId/{id}")
    public List<AnswerDto> getAnswerForQuestion(@PathVariable String id){
        return quesAnsService.getAnswerByQuestionId(id);
    }
}
