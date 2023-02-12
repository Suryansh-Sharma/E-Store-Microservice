package com.suryansh.service;

import com.suryansh.dto.*;
import com.suryansh.entity.*;
import com.suryansh.exception.MicroserviceException;
import com.suryansh.exception.SpringProductException;
import com.suryansh.model.ProductModel;
import com.suryansh.repository.BrandRepository;
import com.suryansh.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final WebClient.Builder webClientBuilder;
    private final ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    @Override
    @Transactional
    @Async
    public CompletableFuture<String> save(ProductModel productModel, String token) {
        Optional<Product> productFromDb = productRepository
                .findByProductName(productModel.getProductName());
        if (productFromDb.isPresent())
            return CompletableFuture.failedFuture(new
                    SpringProductException("Product is already present !!"));
        Optional<Brand> brandOptional = brandRepository.findByName(productModel.getBrandName());
        if (brandOptional.isEmpty()){
            return CompletableFuture.failedFuture(new
                    SpringProductException("Unable to save product because Brand not found."));
        }
        Brand brand = brandOptional.get();
                Description description = Description.builder()
                .data(productModel.getDescription())
                .build();
        Product product = Product.builder()
                .productName(productModel.getProductName())
                .ratings(0)
                .noOfRatings(0)
                .totalRating(0)
                .text(productModel.getText())
                .price(productModel.getPrice())
                .discount(productModel.getDiscount())
                .newPrice(productModel.getNewPrice())
                .productImage(null)
                .productCategory(productModel.getProductCategory())
                .description(description)
                .brand(brand)
                .build();
        description.setProduct(product);
        try {
            productRepository.save(product);
            brand.setNoOfProducts(brand.getNoOfProducts() + 1);
            brandRepository.save(brand);
            log.info("Brand  Saved");
            Product p = productRepository.findByProductName(productModel.getProductName())
                    .orElseThrow();
            // Calling Inventory Microservice for Adding noOfStock of product.
            webClientBuilder.build().post()
                    .uri("http://INVENTORY-SERVICE/api/inventory/addToInventory/"
                            + productModel.getProductName() + "/" + p.getId() + "/" + productModel.getNoOfStock())
                    .header("Authorization", token)
                    .retrieve()
                    .onStatus(HttpStatus::isError,
                            clientResponse -> Mono.error(
                                    new MicroserviceException("Unable to save product because service is down")))
                    .bodyToMono(String.class)
                    .block();
            log.info("Product Saved to Product Microservice and Inventory Service");
            return CompletableFuture.completedFuture("Product placed successfully");
        } catch (Exception e) {
            System.out.println(e);
            throw new SpringProductException("Unable to save Product : ProductServiceImpl.save Catch block");
        }
    }

    @Override
    @Transactional
    public ProductDto fullViewByName(String name) {
        Product product = productRepository.findByProductName(name)
                .orElseThrow(() ->
                        new SpringProductException("Unable to find Product of name:- " +
                                name + ":ProductService.fullViewByName"));
        return getProductFullDetails(product);
    }
    @Override
    @Transactional
    public ProductDto fullViewById(Long id) {
        log.info("Id"+id);
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new SpringProductException("Unable to find Product of id :- " +
                                id + ":ProductService.fullViewById"));
        return getProductFullDetails(product);
    }
    @Transactional
    public ProductDto getProductFullDetails(Product product){
        Brand brand = product.getBrand();
        BrandDto brandDto = BrandDto.builder()
                .id(brand.getBrandId())
                .name(brand.getName())
                .noOfProducts(brand.getNoOfProducts())
                .build();
        List<ProductImageDto> productImages = product.getProductImages().stream()
                .map(this::ImageEntityToDto)
                .toList();
        // Calling Inventory Microservice to get update about stock.
        InventoryResponse productStock = webClientBuilder.build().get()
                .uri("http://INVENTORY-SERVICE/api/inventory/get-product-byId/" + product.getId())
                .retrieve()
                .bodyToMono(InventoryResponse.class)
                .transform(it->{
                    ReactiveCircuitBreaker rcb = reactiveCircuitBreakerFactory.create("PRODUCT-SERVICE");
                    return rcb.run(it,throwable -> Mono.just(InventoryResponse.builder().build()));
                })
                .block();
        assert productStock != null;
        Boolean isInStock = productStock.getNoOfStock() > 0;
        String name = product.getProductName().substring(0,10);
        List<ProductDto> similarProducts =
                productRepository
                .getSimilarProducts(name)
                .stream()
                .map(this::productEntityToDto)
                .toList();
        Description description = product.getDescription();
        DescriptionDto descriptionDto = DescriptionDto.builder()
                .data(description.getData())
                .build();
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
                .description(descriptionDto)
                .brand(brandDto)
                .productImages(productImages)
                .productIsInStock(isInStock)
                .inventoryData(productStock)
                .similarProducts(similarProducts)
                .build();
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
                .imageUrl("http://localhost:8080/api/image/download/"+product.getProductImage())
                .productCategory(product.getProductCategory())
                .build();
    }

    @Override
    @Transactional
    public ProductPagingDto getProductByCategory(String category, Pageable pageable) {
        Page<Product>page=productRepository.findAllByProductCategory(category,pageable);
        List<ProductDto>result=page.getContent()
                .stream()
                .map(this::productEntityToDto)
                .toList();
        return ProductPagingDto.builder()
                .products(result)
                .totalPages(page.getTotalPages())
                .currentPage(pageable.getPageNumber()+1)
                .totalData(page.getTotalElements())
                .build();

    }


    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new SpringProductException("Unable to find Product of id:-  " +
                        id + ":ProductService.findById"));
        return ProductDto.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .imageUrl("http://localhost:8080/api/image/download/"+product.getProductImage())
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
    public ProductPagingDto findByProductNameLike(String productName, Pageable pageable) {
        Page<Product> page = productRepository.findByProductNameContains(productName,pageable);
        List<ProductDto>productDtoList = page.getContent().stream()
                .map(this::productEntityToDto)
                .toList();
        return ProductPagingDto.builder()
                .products(productDtoList)
                .currentPage(pageable.getPageNumber()+1)
                .totalPages(page.getTotalPages())
                .totalData(page.getTotalElements())
                .build();
    }

    @Override
    @Transactional
    public List<NavSearchDto> findProductNameAndId(String productName) {
        return productRepository.findProductNameAndId(productName).stream()
                .map((item)->NavSearchDto.builder()
                        .id(item.getId())
                        .productName(item.getProductName())
                        .build())
                .toList();
    }

    @Override
    @Async
    public CompletableFuture<String> addRatingForProduct(Long id, int rating) {
        Product product = productRepository.findById(id)
                .orElseThrow(()->new SpringProductException("Unable to find product for Rating"));
        if(product.getNoOfRatings()==0){
                product.setRatings(rating);
                product.setNoOfRatings(product.getNoOfRatings()+1);
                product.setTotalRating(rating);
                log.info("new Rating");
        }else{
                int newRating = (product.getTotalRating()+rating) / (product.getNoOfRatings()+1);
                product.setRatings(newRating);
                product.setTotalRating(product.getTotalRating()+rating);
                product.setNoOfRatings(product.getNoOfRatings()+1);
        }
        try {
            productRepository.save(product);
            return CompletableFuture.completedFuture("Review Added Successfully");
        }catch (Exception e){
            return CompletableFuture.failedFuture(new
                    SpringProductException("Unable to add review for Product"));
        }
    }

    private ProductDto productEntityToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .imageUrl("http://localhost:8080/api/image/download/"+product.getProductImage())
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
    private ProductImageDto ImageEntityToDto(ProductImages image) {
        ProductImageDto res = new ProductImageDto();
        res.setImageUrl(image.getImageName());
        res.setImageName(image.getImageName());
        res.setProductId(image.getProductId());
        res.setId(image.getId());
        return res;
    }
}
