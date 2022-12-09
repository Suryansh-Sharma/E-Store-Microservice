package com.suryansh.service;

import com.suryansh.dto.BrandDto;
import com.suryansh.model.BrandModel;

import java.util.List;

public interface BrandService {
    void save(BrandModel brandModel);

    List<BrandDto> getAllBrands();

    BrandDto findByName(String name);
}
