package com.suryansh.service;

import com.suryansh.dto.NavSearchDto;
import com.suryansh.dto.ProductDto;
import com.suryansh.dto.ProductPagingDto;
import com.suryansh.model.ProductModel;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProductService {
    CompletableFuture<String> save(ProductModel productModel, String token);

    ProductDto fullViewByName(String name);

    ProductDto fullViewById(Long id);

    ProductDto getProductByName(String name);

    ProductPagingDto getProductByCategory(String category, Pageable pageable);


    ProductDto getProductById(Long id);

    ProductPagingDto findByProductNameLike(String productName, Pageable pr);

    List<NavSearchDto> findProductNameAndId(String productName);

    CompletableFuture<String> addRatingForProduct(Long id, int rating);

}
