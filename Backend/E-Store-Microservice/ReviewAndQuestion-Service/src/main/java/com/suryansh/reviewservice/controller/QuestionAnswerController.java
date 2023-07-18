package com.suryansh.reviewservice.controller;

import com.suryansh.reviewservice.dto.PagingQuestionDto;
import com.suryansh.reviewservice.model.AnswerModel;
import com.suryansh.reviewservice.model.QuestionModel;
import com.suryansh.reviewservice.service.QuesAnsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/question-ans")
@CrossOrigin("*")
public class QuestionAnswerController {
    private final QuesAnsService quesAnsService;
    public QuestionAnswerController(QuesAnsService quesAnsService) {
        this.quesAnsService = quesAnsService;
    }

    @PostMapping("/add-new-question")
    @SecurityRequirement(name = "bearerAuth")
    public String addNewQuestion(@RequestBody @Valid QuestionModel questionModel){
        return quesAnsService.addQuestion(questionModel);
    }
    @PostMapping("/add-answer")
    @SecurityRequirement(name = "bearerAuth")
    public String addAnswer(@RequestBody @Valid AnswerModel answerModel){
        return quesAnsService.addAnswer(answerModel);
    }
    @GetMapping("/get-by-productId/{productId}")
    public PagingQuestionDto getQuestionByProductId(@PathVariable Long productId,
                                                    @RequestParam(name = "pageNo",defaultValue = "0",required = false)
                                                    int pageNo){
        Pageable pageable = PageRequest.of(pageNo,6);
        return quesAnsService.getByProductId(productId,pageable);
    }
    @PutMapping("/update-question/{quesId}")
    @SecurityRequirement(name = "bearerAuth")
    public String updateQuestion(@Valid @RequestBody QuestionModel model,@PathVariable String quesId){
        return quesAnsService.updateQuestion(model,quesId);
    }

}
