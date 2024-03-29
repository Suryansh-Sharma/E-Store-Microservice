package com.suryansh.orderservice.entity;

import lombok.*;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "OrderTbl")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Instant orderDate;
    private Instant lastUpdate;
    private String status;
    private int totalItems;
    private Float price;
    private Boolean isProductDelivered = false;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "orderId",referencedColumnName = "id")
    private List<OrderItem> orderItems;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private OrderAddress orderAddress;
}
