package com.suryansh.userservice.repository;

import com.suryansh.userservice.entity.LikedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedProductRepository extends JpaRepository<LikedProduct, Long> {
    Optional<LikedProduct> findByUserIdAndProductId(Long id, Long productId);
}