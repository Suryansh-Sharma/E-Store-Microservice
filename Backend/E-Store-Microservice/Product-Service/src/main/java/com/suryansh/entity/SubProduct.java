package com.suryansh.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubProduct {
    @Id
    private Long id;
    private Long productId;
    private String subProductName;
    private String productImage;
    private Float price;
}
