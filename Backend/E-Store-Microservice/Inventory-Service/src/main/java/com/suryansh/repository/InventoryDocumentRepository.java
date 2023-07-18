package com.suryansh.repository;

import com.suryansh.entity.InventoryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface InventoryDocumentRepository extends MongoRepository<InventoryDocument,String> {
    Optional<InventoryDocument> findByProduct_ProductId(Long productId);

    Optional<InventoryDocument> findByProduct_Title(String product);
}
