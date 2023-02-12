package com.suryansh.service;

import com.suryansh.dto.BrandDto;
import com.suryansh.dto.ProductDto;
import com.suryansh.dto.ProductPagingDto;
import com.suryansh.entity.Brand;
import com.suryansh.entity.Product;
import com.suryansh.exception.SpringProductException;
import com.suryansh.model.BrandModel;
import com.suryansh.repository.BrandRepository;
import com.suryansh.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
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
        } catch (Exception e) {
            throw new SpringProductException("Unable to save Brand !! From BrandServiceImpl save");
        }
    }

    @Override
    @Transactional
    public ProductPagingDto findByName(String name, Pageable pageable) {
        Brand brand = brandRepository.findByName(name)
                .orElseThrow(() -> new SpringProductException("Unable to find Brand by name From BrandServiceImpl"));
        Page<Product>res = productRepository.findByBrand(brand,pageable);
        List<ProductDto> products = res.getContent()
                .stream()
                .map(this::productEntityToDto)
                .toList();
        return ProductPagingDto.builder()
                .products(products)
                .currentPage(pageable.getPageNumber()+1)
                .totalPages(res.getTotalPages())
                .totalData(res.getTotalElements())
                .build();
    }

    @Override
    public List<BrandDto> findByNameLike(String name) {
        return brandRepository.findBrandByName(name).stream()
                .map((brand ->BrandDto.builder()
                        .id(brand.getBrandId())
                        .name(brand.getName())
                        .noOfProducts(brand.getNoOfProducts())
                        .build()))
                .toList();

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
                .imageUrl("http://localhost:8080/api/image/download/"+product.getProductImage())
                .productCategory(product.getProductCategory())
                .build();
    }
}
