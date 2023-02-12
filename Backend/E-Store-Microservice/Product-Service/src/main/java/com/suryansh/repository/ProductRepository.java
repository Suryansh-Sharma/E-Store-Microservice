package com.suryansh.repository;

import com.suryansh.entity.Brand;
import com.suryansh.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductName(String name);

    Page<Product> findAllByProductCategory(String category, Pageable pageable);

    Page<Product> findByProductNameContains(String productName, Pageable pageable);


    @Query(value = "SELECT * FROM public.product WHERE product_name ILIKE %:productName% LIMIT 5 ",nativeQuery = true)
    List<Product> findProductNameAndId(String productName);

    Page<Product> findByBrand(Brand brand, Pageable pageable);
    @Query(value = "SELECT * FROM public.product WHERE product_name LIKE :name%",nativeQuery = true)
    List<Product> getSimilarProducts(String name);
}