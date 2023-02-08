package com.suryansh.orderservice.entity;

import lombok.*;

import javax.persistence.*;

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
    private Long userId;
    private String line1;
    private String city;
    private int pinCode;
    private String otherDetails;
}
