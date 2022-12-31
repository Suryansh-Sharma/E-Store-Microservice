package com.suryansh.reviewservice.repository;

import com.suryansh.reviewservice.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends MongoRepository<Question,String> {
    Page<Question> findByProductId(Long productId, Pageable pageable);
}
