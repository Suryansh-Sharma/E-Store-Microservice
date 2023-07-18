package com.suryansh.orderservice.entity;

import lombok.*;

import jakarta.persistence.*;

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
    private String pinCode;
    private String otherDetails;
}
