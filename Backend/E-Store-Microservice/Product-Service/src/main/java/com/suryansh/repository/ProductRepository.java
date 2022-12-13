package com.suryansh.repository;

import com.suryansh.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductName(String name);

    List<Product> findAllByProductCategory(String category, Pageable pageable);

    List<Product> findByProductNameContains(String productName);

    Product findTopByOrderByIdDesc();
}