package com.suryansh.repository;

import com.suryansh.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface BrandRepository extends JpaRepository<Brand, Long> {

    Optional<Brand> findByName(String name);
    @Query(value = "SELECT b FROM Brand b WHERE b.name LIKE %:name%")
    List<Brand> findBrandByName(String name);
}