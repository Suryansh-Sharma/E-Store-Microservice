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

    Page<Product> findAllByProductCategory(String category, Pageable pageable);

    Page<Product> findByProductNameContains(String productName, Pageable pageable);

    Product findTopByOrderByIdDesc();

    @Query(value = "SELECT * FROM public.product WHERE product_name ILIKE %:productName% LIMIT 10 ",nativeQuery = true)
    List<Product> findProductNameAndId(String productName);
}