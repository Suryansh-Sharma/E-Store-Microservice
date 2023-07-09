package com.suryansh.orderservice.dto;

public record OrderItemsDto (
    Long itemId,
     Long orderId,
     Long productId,
     String productName,
     int quantity,
     Float price){
}
