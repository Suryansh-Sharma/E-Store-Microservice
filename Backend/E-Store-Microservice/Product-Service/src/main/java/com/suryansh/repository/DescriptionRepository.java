package com.suryansh.repository;

import com.suryansh.entity.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DescriptionRepository extends JpaRepository<Description, Long> {

    Optional<Description> findTopByOrderByIdDesc();
}