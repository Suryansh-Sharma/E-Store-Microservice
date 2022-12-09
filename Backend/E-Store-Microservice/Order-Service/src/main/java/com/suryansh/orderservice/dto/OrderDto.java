package com.suryansh.orderservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private Long userId;
    private String orderDate;
    private String lastUpdate;
    private String status;
    private int totalItems;
    private Float price;
    private Boolean isProductDelivered;
}
