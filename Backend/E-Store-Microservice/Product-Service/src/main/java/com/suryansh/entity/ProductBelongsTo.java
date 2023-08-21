package com.suryansh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductBelongsTo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int totalProducts;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "belong_id",referencedColumnName = "id")
    private List<Product>products;
}
