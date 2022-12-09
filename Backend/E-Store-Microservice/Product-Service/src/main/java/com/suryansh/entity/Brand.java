package com.suryansh.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandId;
    @NotBlank(message = "Brand Name can't be Empty")
    private String name;
    private int noOfProducts;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "brandId", referencedColumnName = "brandId")
    private List<Product> products;
}
