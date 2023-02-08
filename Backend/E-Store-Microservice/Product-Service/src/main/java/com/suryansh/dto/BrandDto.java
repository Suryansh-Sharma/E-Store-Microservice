package com.suryansh.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class BrandDto {
    private Long id;
    private String name;
    private int noOfProducts;
}
