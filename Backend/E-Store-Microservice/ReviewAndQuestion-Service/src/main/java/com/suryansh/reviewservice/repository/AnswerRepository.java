package com.suryansh.reviewservice.repository;

import com.suryansh.reviewservice.entity.Answer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AnswerRepository extends MongoRepository<Answer,String> {
    List<Answer> findByQuestionId(String id);
}
