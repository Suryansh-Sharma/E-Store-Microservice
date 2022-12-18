package com.suryansh.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class ProductPagingDto {
    private List<ProductDto> products;
    private int currentPage;
    private int totalPages;
    private Long totalData;

}
