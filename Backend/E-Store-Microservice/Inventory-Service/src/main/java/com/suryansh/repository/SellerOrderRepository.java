package com.suryansh.repository;

import com.suryansh.entity.SellerOrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SellerOrderRepository extends MongoRepository<SellerOrderDocument,String> {
}
