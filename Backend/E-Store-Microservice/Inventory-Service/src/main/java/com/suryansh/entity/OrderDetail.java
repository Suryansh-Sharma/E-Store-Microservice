package com.suryansh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
@Data
@AllArgsConstructor
public class OrderDetail {
    private long userId;
    private Instant placedOn;
    private status status;
    private long productId;
    private Float price;
    private int quantity;
    private Float totalPrice;

    public enum status{
        PLACED,
        DELIVERD,
        PENDING,
        CANCELED
    }
}
