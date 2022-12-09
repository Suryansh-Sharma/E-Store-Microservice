package com.suryansh.entity;

import javax.persistence.*;

import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Description {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String data;

    private Instant releaseDate;
    private String dimensions;
    private String specialFeatures;
}