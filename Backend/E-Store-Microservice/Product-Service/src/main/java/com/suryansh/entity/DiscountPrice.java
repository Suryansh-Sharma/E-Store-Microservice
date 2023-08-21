package com.suryansh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "discount_price")
public class DiscountPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Double value;
    private String currency;
    private Float percentage;

    // Product
    @OneToOne(mappedBy = "discountPrice",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Product product;

}
