package com.suryansh.userservice.dto;

import java.util.List;

public record LikedProductPaging (List<ProductDto> products,int currentPage,int totalPages){
}
