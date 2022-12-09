package com.suryansh.orderservice.repository;

import com.suryansh.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findTopByOrderByIdDesc();

    List<Order> findByUserIdOrderByIdDesc(Long id);

    List<Order> findByIsProductDelivered(Boolean isProductDelivered);
}