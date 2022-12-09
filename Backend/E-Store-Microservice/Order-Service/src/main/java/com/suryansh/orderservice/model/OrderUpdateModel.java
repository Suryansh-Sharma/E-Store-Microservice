package com.suryansh.orderservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderUpdateModel {
    private Long orderId;
    private Boolean isProductDelivered = false;
    private String status;
}
