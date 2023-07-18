package com.suryansh.entity;

import lombok.*;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String subTitle;
    private String shortDescription;
    // Price
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "price_id")
    private Price price;

    private String categoryPath;
    private String imageUrl;
    // Additional Image
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id",referencedColumnName = "id")
    private List<ProductImage>productImages;

    private String color;
    // Brand
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id",referencedColumnName = "id")
    private Brand brand;

    private String itemWebUrl;
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "belong_id",referencedColumnName = "id")
    private ProductBelongsTo belongsTo;


}
