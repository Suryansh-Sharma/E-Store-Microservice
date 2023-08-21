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
@Table(name = "product_rich_text")
public class ProductRichText {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    private String text1;
    @Lob
    private String text2;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_rich_text_id")
    private Product product;
}
