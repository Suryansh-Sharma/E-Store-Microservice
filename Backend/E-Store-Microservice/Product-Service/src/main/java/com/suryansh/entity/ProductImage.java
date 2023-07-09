package com.suryansh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private imageType imageType;

    private enum imageType{
        POSTER,
        SLIDE,
        DESCRIPTION
    }
}


