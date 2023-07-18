package com.suryansh.repository;

import com.suryansh.entity.Brand;
import com.suryansh.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByBrand(Brand brand, Pageable pageable);

    Optional<Product> findByTitle(String title);
}