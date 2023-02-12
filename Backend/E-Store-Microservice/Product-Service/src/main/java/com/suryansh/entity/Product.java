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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Product name can't be blank.!!")
    private String productName;
    private int ratings;
    private int noOfRatings;
    private int totalRating;
    private String text;
    private Float price;
    private int discount;
    private Float newPrice;
    private String productImage;
    private String productCategory;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, optional = false)
    private Description description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "productId",referencedColumnName = "id")
    private List<ProductImages>productImages;
}
