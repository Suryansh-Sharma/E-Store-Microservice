package com.suryansh.userservice.dto;

import com.suryansh.userservice.entity.LikedProduct;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikedProductPaging {
    List<ProductDto> products;
    private int currentPage;
    private int totalPages;
}
