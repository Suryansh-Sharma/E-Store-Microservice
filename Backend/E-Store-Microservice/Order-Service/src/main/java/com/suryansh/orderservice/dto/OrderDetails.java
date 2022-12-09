package com.suryansh.orderservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetails {
    private Long orderId;
    private String status;
    private int totalItems;
    private Float price;
    private String line1;
    private String city;
    private int pinCode;
    private String otherDetails;
    private Boolean isProductDelivered = false;
    private List<OrderItemsDto> orderItems;
}
