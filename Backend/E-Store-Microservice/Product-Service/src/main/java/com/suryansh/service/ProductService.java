package com.suryansh.service;

import com.suryansh.dto.ProductDto;
import com.suryansh.dto.ProductPagingDto;
import com.suryansh.entity.Product;
import com.suryansh.model.ProductModel;
import com.suryansh.model.SubProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    void save(ProductModel productModel);

    ProductDto fullViewByName(String name);

    ProductDto getProductByName(String name);

    ProductPagingDto getProductByCategory(String category, Pageable pageable);

    void saveSubProduct(SubProductModel model);

    ProductDto getProductById(Long id);

    List<ProductDto> findByProductNameLike(String productName, PageRequest pr);
}
