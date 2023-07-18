package com.suryansh.repository;

import com.suryansh.entity.Seller;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SellerDocumentRepository extends MongoRepository<Seller,String> {
    Optional<Seller> findByUserId(Long userId);
}
