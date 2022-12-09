package com.suryansh.repository;

import com.suryansh.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductName(String productName);

    Optional<Inventory> findByProductId(Long productId);
}