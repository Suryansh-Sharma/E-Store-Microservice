package com.suryansh.service;

import com.suryansh.dto.BrandDto;
import com.suryansh.dto.ProductPagingDto;
import com.suryansh.model.BrandModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandService {
    void save(BrandModel brandModel);


    ProductPagingDto findByName(String name, Pageable pageable);

    List<BrandDto> findByNameLike(String name);
}
