package com.suryansh.service;

import com.suryansh.dto.NavSearchDto;
import com.suryansh.dto.ProductDto;
import com.suryansh.dto.ProductPagingDto;
import com.suryansh.model.ProductModel;
import com.suryansh.model.SubProductModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    void save(ProductModel productModel, String token);

    ProductDto fullViewByName(String name, String token);

    ProductDto fullViewById(Long id, String token);

    ProductDto getProductByName(String name);

    ProductPagingDto getProductByCategory(String category, Pageable pageable);

    void saveSubProduct(SubProductModel model, String token);

    ProductDto getProductById(Long id);

    ProductPagingDto findByProductNameLike(String productName, Pageable pr);

    List<NavSearchDto> findProductNameAndId(String productName);
}
