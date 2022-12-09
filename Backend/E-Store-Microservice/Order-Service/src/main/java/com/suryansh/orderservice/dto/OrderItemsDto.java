package com.suryansh.orderservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsDto {
    private Long itemId;
    private Long orderId;
    private Long productId;
    private String productName;
    private int quantity;
    private Float price;
}
