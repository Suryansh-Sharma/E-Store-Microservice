package com.suryansh.userservice.repository;

import com.suryansh.userservice.entity.LikedProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikedProductRepository extends JpaRepository<LikedProduct, Long> {
    Optional<LikedProduct> findByUserIdAndProductId(Long id, Long productId);

    Page<LikedProduct> findByUserId(Long id, Pageable pageable);
}