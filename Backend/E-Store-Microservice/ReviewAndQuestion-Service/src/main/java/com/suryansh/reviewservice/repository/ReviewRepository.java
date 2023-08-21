package com.suryansh.reviewservice.repository;

import com.suryansh.reviewservice.entity.Review;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ReviewRepository extends MongoRepository<Review,String> {
    Review getReviewByProductId(long productId);
    Optional<Review> findByProductId(Long productId);
    @Aggregation(pipeline = {
            "{$match: { productId: ?0 }}",
            "{$unwind: '$allReviews'}", // Unwind the array for paging
            "{$sort: { 'allReviews.dateOfReview': -1 }}",
            "{$skip: ?1}",
            "{$limit: ?2}"
    })
    List<Review.AllReviews> getPaginatedReviewsForProduct(Long productId, int skip, int limit);

}
