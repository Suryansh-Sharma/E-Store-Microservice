package com.suryansh.reviewservice.repository;

import com.suryansh.reviewservice.entity.ProductRating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends MongoRepository<ProductRating,String> {
    Optional<ProductRating> findByProductId(Long id);
}
