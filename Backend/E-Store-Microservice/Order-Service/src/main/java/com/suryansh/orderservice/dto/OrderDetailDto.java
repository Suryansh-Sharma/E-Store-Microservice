package com.suryansh.orderservice.dto;


import java.util.List;

public record OrderDetails (
     Long orderId,
     String status,
     int totalItems,
     Float price,
     String line1,
     String city,
     String pinCode,
     String otherDetails,
     Boolean isProductDelivered,
     List<OrderItemsDto> orderItems
){}
