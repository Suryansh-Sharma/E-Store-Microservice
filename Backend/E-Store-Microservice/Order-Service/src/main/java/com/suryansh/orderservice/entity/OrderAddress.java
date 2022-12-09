package com.suryansh.orderservice.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "OrderAddressTbl")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private Long userId;
    private String line1;
    private String city;
    private int pinCode;
    private String otherDetails;
}
