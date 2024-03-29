package com.suryansh.orderservice.entity;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "OrderItemTbl")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;
    private Long orderId;
    private Long productId;
    private String productName;
    private int quantity;
    private Float price;
}
