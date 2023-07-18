package com.suryansh.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private Float price;
    private Float totalPrice;
    private int noOfProduct;

    @ManyToOne
    private User user;
}
