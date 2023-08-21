package com.suryansh.service;

import com.suryansh.dto.ProductDto;
import com.suryansh.dto.ProductFullDto;
import com.suryansh.dto.ProductPagingDto;
import com.suryansh.model.ProductModel;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProductService {
    CompletableFuture<String> save(ProductModel productModel);

    CompletableFuture<ProductFullDto> fullViewByName(String name);

    CompletableFuture<ProductFullDto> fullViewById(Long id);

    ProductDto getProductByName(String name);

    ProductDto getProductById(Long id);


    ProductModel testInventory(ProductModel productModel);

    String createRelationshipForProductAndRelatedProduct(long parentProductId, long childProductId);

    List<ProductDto> getBelongsProductFromDb(long parentId);

    ProductPagingDto getRelatedProductByCategory(String categoryTree, int pageNo, int pageSize);
}
