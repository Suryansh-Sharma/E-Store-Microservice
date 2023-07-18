package com.suryansh.repository;

import com.suryansh.entity.ProductBelongsTo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductBelongsToRepository extends JpaRepository<ProductBelongsTo, Long> {
}