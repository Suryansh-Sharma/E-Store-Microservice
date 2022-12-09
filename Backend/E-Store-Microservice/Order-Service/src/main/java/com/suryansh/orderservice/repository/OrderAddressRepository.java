package com.suryansh.orderservice.repository;

import com.suryansh.orderservice.entity.OrderAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderAddressRepository extends JpaRepository<OrderAddress, Long> {
    OrderAddress findByOrderId(Long orderId);
}