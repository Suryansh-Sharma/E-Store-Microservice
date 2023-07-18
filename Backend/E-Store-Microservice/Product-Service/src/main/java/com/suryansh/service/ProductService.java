package com.suryansh.service;

import com.suryansh.dto.NavSearchDto;
import com.suryansh.dto.ProductDto;
import com.suryansh.dto.ProductFullDto;
import com.suryansh.dto.ProductPagingDto;
import com.suryansh.model.ProductModel;
import com.suryansh.model.ProductSearchCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProductService {
    CompletableFuture<String> save(ProductModel productModel, String token);

    CompletableFuture<ProductFullDto> fullViewByName(String name);

    CompletableFuture<ProductFullDto> fullViewById(Long id);

    ProductDto getProductByName(String name);

    ProductDto getProductById(Long id);


}
