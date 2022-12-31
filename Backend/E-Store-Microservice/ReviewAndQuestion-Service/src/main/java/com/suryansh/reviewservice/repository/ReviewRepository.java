package com.suryansh.reviewservice.repository;

import com.suryansh.reviewservice.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReviewRepository extends MongoRepository<Review,String> {
    Page<Review> findByProductId(Long productId, Pageable pageable);

    List<Review> findByUserName(String userName);
}
