package com.suryansh.service;

import com.suryansh.dto.BrandDto;
import com.suryansh.dto.ProductPagingDto;
import com.suryansh.entity.Brand;
import com.suryansh.entity.Product;
import com.suryansh.exception.SpringProductException;
import com.suryansh.mapper.ProductMapper;
import com.suryansh.model.BrandModel;
import com.suryansh.repository.BrandRepository;
import com.suryansh.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private static final Logger logger = LoggerFactory.getLogger(BrandServiceImpl.class);

    @Override
    @Transactional
    public void save(BrandModel brandModel) {
        try {
            Optional<Brand> checkBrand = brandRepository.findByName(brandModel.getName());
            if (checkBrand.isPresent()) throw new
                    SpringProductException("Brand is Already Present !!");
            Brand brand = Brand.builder()
                    .name(brandModel.getName())
                    .noOfProducts(0)
                    .build();
            brandRepository.save(brand);
            logger.info("Successfully added new brand {} ",brand.getName());
        } catch (Exception e) {
            logger.error("Unable to save brand {} ",brandModel.getName()+e);
            throw new SpringProductException("Unable to save Brand !! From BrandServiceImpl save");
        }
    }

    @Override
    @Transactional
    public ProductPagingDto findByName(String name, Pageable pageable) {
        Brand brand = brandRepository.findByName(name)
                .orElseThrow(() -> new SpringProductException("Unable to find Brand by name From BrandServiceImpl"));
        Page<Product>res = productRepository.findByBrand(brand,pageable);
        return new ProductPagingDto(
                pageable.getPageNumber(),
                res.getTotalPages(),
                res.get()
                        .map(productMapper::convertProductEntityToDto)
                        .toList(),
                res.getTotalElements()
        );
    }

    @Override
    public List<BrandDto> findByNameLike(String name) {
        return brandRepository.findBrandByName(name).stream()
                .map(brand ->BrandDto.builder()
                        .id(brand.getId())
                        .name(brand.getName())
                        .noOfProducts(brand.getNoOfProducts())
                        .build())
                .toList();

    }

}
