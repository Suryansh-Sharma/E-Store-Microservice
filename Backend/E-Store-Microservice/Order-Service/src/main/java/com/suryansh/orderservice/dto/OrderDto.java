package com.suryansh.orderservice.dto;

public record OrderDto (
        Long id,
         Long userId,
         String orderDate,
         String lastUpdate,
         String status,
         int totalItems,
         Float price,
         Boolean isProductDelivered
){}
