package com.suryansh.orderservice.repository;

import com.suryansh.orderservice.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {
}