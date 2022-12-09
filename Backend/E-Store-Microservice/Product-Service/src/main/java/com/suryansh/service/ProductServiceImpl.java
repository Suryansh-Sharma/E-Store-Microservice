package com.suryansh.service;

import com.suryansh.dto.*;
import com.suryansh.entity.*;
import com.suryansh.exception.SpringProductException;
import com.suryansh.model.ProductModel;
import com.suryansh.model.SubProductModel;
import com.suryansh.repository.BrandRepository;
import com.suryansh.repository.DescriptionRepository;
import com.suryansh.repository.ProductRepository;
import com.suryansh.repository.SubProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final BrandRepository brandRepository;
    private final DescriptionRepository descriptionRepository;
    private final ProductRepository productRepository;
    private final SubProductRepository subProductRepository;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public ProductServiceImpl(BrandRepository brandRepository, DescriptionRepository descriptionRepository, ProductRepository productRepository, SubProductRepository subProductRepository, WebClient.Builder webClientBuilder) {
        this.brandRepository = brandRepository;
        this.descriptionRepository = descriptionRepository;
        this.productRepository = productRepository;
        this.subProductRepository = subProductRepository;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    @Transactional
    @Async
    public void save(ProductModel productModel) {
        Brand brand = brandRepository.findByName(productModel.getBrandName())
                .orElseThrow(() -> new SpringProductException("Unable to find Brand : ProductServiceImpl.save"));

        Description description = Description.builder()
                .data(productModel.getData())
                .releaseDate(Instant.now())
                .dimensions(productModel.getDimensions())
                .specialFeatures(productModel.getSpecialFeatures())
                .build();
        descriptionRepository.save(description);
        Description productDescription = descriptionRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new SpringProductException("Unable to get description"));

        Product product = Product.builder()
                .productName(productModel.getProductName())
                .ratings(0)
                .noOfRatings(0)
                .text(productModel.getText())
                .price(productModel.getPrice())
                .discount(productModel.getDiscount())
                .newPrice(productModel.getNewPrice())
                .productImage(productModel.getProductImage())
                .productCategory(productModel.getProductCategory())
                .description(productDescription)
                .brand(brand)
                .build();
        try {
            productRepository.save(product);
            brand.setNoOfProducts(brand.getNoOfProducts() + 1);
            brandRepository.save(brand);
            Product p = productRepository.findByProductName(productModel.getProductName())
                    .orElseThrow();
            // Calling Inventory Microservice.
            String response = webClientBuilder.build().post()
                    .uri("http://geekyprogrammer:8080/api/inventory/addToInventory/"
                            + productModel.getProductName() + "/" + p.getId() + productModel.getNoOfStock())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info(response);
            log.info("Product Saved to Product Microservice and Inventory Service");
        } catch (Exception e) {
            throw new SpringProductException("Unable to save Product : ProductServiceImpl.save Catch block");
        }
    }

    @Override
    @Transactional
    public ProductDto fullViewByName(String name) {
        Product product = productRepository.findByProductName(name)
                .orElseThrow(() -> new SpringProductException("Unable to find Product :ProductService.findByName"));
        Brand brand = product.getBrand();
        BrandDto brandDto = BrandDto.builder()
                .id(brand.getBrandId())
                .name(brand.getName())
                .noOfProducts(brand.getNoOfProducts())
                .products(null)
                .build();
        List<ProductImageDto> productImages = product.getProductImages().stream()
                .map(this::ImageEntityToDto)
                .toList();

        // Calling Inventory Microservice
        InventoryResponse productStock = webClientBuilder.build().get()
                .uri("http://geekyprogrammer:8080/api/inventory/get-product-byId/" + product.getId())
                .retrieve()
                .bodyToMono(InventoryResponse.class)
                .block();
        assert productStock != null;
        Boolean isInStock = productStock.getNoOfStock() > 0;
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
                .description(product.getDescription())
                .brand(brandDto)
                .productIsInStock(isInStock)
                .productImages(productImages)
                .build();
    }

    private ProductImageDto ImageEntityToDto(ProductImages image) {
        ProductImageDto res = new ProductImageDto();
        res.setImageUrl(image.getImageName());
        res.setImageName(image.getImageName());
        res.setProductId(image.getProductId());
        res.setId(image.getId());
        return res;
    }

    @Override
    @Transactional
    public ProductDto getProductByName(String name) {
        Product product = productRepository.findByProductName(name)
                .orElseThrow(() -> new SpringProductException("Unable to find Product :ProductService.findByName"));

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

    @Override
    @Transactional
    public ProductPagingDto getProductByCategory(String category, Pageable pageable) {
        List<ProductDto>result=productRepository.findAllByProductCategory(category,pageable)
                .stream()
                .map((product -> ProductDto.builder()
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
                        .build()))
                .toList();
        if (result.size()==0)return null;
        return ProductPagingDto.builder()
                .products(result)
                .currentPage(pageable.getPageNumber())
                .build();

    }

    @Override
    @Transactional
    @Async
    public void saveSubProduct(SubProductModel val) {
        try {
            SubProduct subProduct = SubProduct.builder()
                    .productId(val.getProductId())
                    .subProductName(val.getSubProductName())
                    .price(val.getPrice())
                    .build();
            Brand brand = brandRepository.findByName(val.getBrandName())
                    .orElseThrow(() -> new
                            SpringProductException("Unable to find Brand : ProductServiceImpl.saveSubProduct"));
            Product product = Product.builder()
                    .productName(val.getSubProductName())
                    .ratings(0)
                    .noOfRatings(0)
                    .text(val.getText())
                    .price(val.getPrice())
                    .discount(val.getDiscount())
                    .newPrice(val.getNewPrice())
                    .productImage(val.getProductImage())
                    .brand(brand)
                    .build();
            // Calling Inventory Microservice.
            webClientBuilder.build().post()
                    .uri("http://geekyprogrammer:8080/api/inventory/addToInventory/"
                            + val.getSubProductName() + "/" + val.getNoOfStock())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            brand.setNoOfProducts(brand.getNoOfProducts()+1);
            brandRepository.save(brand);
            subProductRepository.save(subProduct);
            productRepository.save(product);
            log.info("SubProduct Saved SuccessFully");
        } catch (Exception e) {
            throw new SpringProductException("Unable to Save Product SubProduct : ");
        }
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new SpringProductException("Unable to find Product :ProductService.findById"));
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

    @Override
    @Transactional
    public List<ProductDto> findByProductNameLike(String productName, PageRequest pr) {
        return productRepository.findByProductNameContains(productName)
                .stream()
                .map((product -> ProductDto.builder()
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
                        .build()))
                .toList();
    }
}