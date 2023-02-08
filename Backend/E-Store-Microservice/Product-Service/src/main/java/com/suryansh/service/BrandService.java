package com.suryansh.service;

import com.suryansh.dto.ProductPagingDto;
import com.suryansh.model.BrandModel;
import org.springframework.data.domain.Pageable;

public interface BrandService {
    void save(BrandModel brandModel);


    ProductPagingDto findByName(String name, Pageable pageable);
}
