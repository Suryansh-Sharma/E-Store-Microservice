package com.suryansh.service;

import com.suryansh.dto.BrandDto;
import com.suryansh.dto.ProductDto;
import com.suryansh.entity.Brand;
import com.suryansh.entity.Product;
import com.suryansh.exception.SpringProductException;
import com.suryansh.model.BrandModel;
import com.suryansh.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Override
    @Transactional
    public void save(BrandModel brandModel) {
        try {
            Optional<Brand> checkBrand = brandRepository.findByName(brandModel.getName());
            if (checkBrand.isPresent()) throw new
                    SpringProductException("Brand is Already Present !! From BrandServiceImpl save");
            Brand brand = Brand.builder()
                    .name(brandModel.getName())
                    .noOfProducts(0)
                    .build();
            brandRepository.save(brand);
        } catch (Exception e) {
            throw new SpringProductException("Unable to save Brand !! From BrandServiceImpl save");
        }
    }

    @Override
    public List<BrandDto> getAllBrands() {
        return brandRepository.findAll()
                .stream().map(this::brandEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BrandDto findByName(String name) {
        Brand brand = brandRepository.findByName(name)
                .orElseThrow(() -> new SpringProductException("Unable to find Brand by name From BrandServiceImpl"));
        List<ProductDto> products = brand.getProducts()
                .stream()
                .map(this::productEntityToDto)
                .collect(Collectors.toList());
        return BrandDto.builder()
                .id(brand.getBrandId())
                .name(brand.getName())
                .noOfProducts(brand.getNoOfProducts())
                .products(products)
                .build();
    }

    private BrandDto brandEntityToDto(Brand brand) {
        return BrandDto.builder()
                .id(brand.getBrandId())
                .name(brand.getName())
                .noOfProducts(brand.getNoOfProducts())
                .products(null)
                .build();
    }

    private ProductDto productEntityToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .ratings(product.getRatings())
                .noOfRatings(product.getNoOfRatings())
                .text(product.getText())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .newPrice(product.getNewPrice())
                .productImage(product.getProductImage())
                .productCategory(product.getProductCategory())
                .build();
    }
}
