package com.suryansh.userservice.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class UserCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private Float price;
    private Float totalPrice;
    private int noOfProduct;

}
