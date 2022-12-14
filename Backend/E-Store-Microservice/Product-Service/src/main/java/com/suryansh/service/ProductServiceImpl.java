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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final BrandRepository brandRepository;
    private final DescriptionRepository descriptionRepository;
    private final ProductRepository productRepository;
    private final SubProductRepository subProductRepository;
    private final WebClient.Builder webClientBuilder;
    private final ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    @Override
    @Transactional
    @Async
    public void save(ProductModel productModel, String token) {
        log.info("Searching Brand");
        Brand brand = brandRepository.findByName(productModel.getBrandName())
                .orElseThrow(() -> new SpringProductException("Unable to find Brand : ProductServiceImpl.save"));
        log.info("Brand Find");
        Description description = Description.builder()
                .data(productModel.getDescription())
                .build();
        descriptionRepository.save(description);
        log.info("Description Saved");

        Description productDescription = descriptionRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new SpringProductException("Unable to get description"));
        log.info("Description Find");

        Product product = Product.builder()
                .productName(productModel.getProductName())
                .ratings(0)
                .noOfRatings(0)
                .totalRating(0)
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
            log.info("Product  Saved");
            brand.setNoOfProducts(brand.getNoOfProducts() + 1);
            brandRepository.save(brand);
            log.info("Brand  Saved");
            Product p = productRepository.findByProductName(productModel.getProductName())
                    .orElseThrow();
            log.info("Product  Found");
            // Calling Inventory Microservice for Adding noOfStock of product.
            webClientBuilder.build().post()
                    .uri("http://localhost:8080/api/inventory/addToInventory/"
                            + productModel.getProductName() + "/" + p.getId() +"/"+ productModel.getNoOfStock())
                    .header("Authorization",token)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("Product Saved to Product Microservice and Inventory Service");
        } catch (Exception e) {
            throw new SpringProductException("Unable to save Product : ProductServiceImpl.save Catch block");
        }
    }

    @Override
    @Transactional
    public ProductDto fullViewByName(String name, String token) {
        Product product = productRepository.findByProductName(name)
                .orElseThrow(() -> new SpringProductException("Unable to find Product :ProductService.findByName"));
        return getProductFullDetails(product,token);
    }
    @Override
    @Transactional
    public ProductDto fullViewById(Long id, String token) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new SpringProductException("Unable to find Product :ProductService.findByName"));
        return getProductFullDetails(product,token);
    }
    @Transactional
    public ProductDto getProductFullDetails(Product product, String token){
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
        List<SubProductDto> subProductDtoList= product.getSubProducts().stream()
                .map(this::subProductEntityToDto)
                .toList();
        // Calling Inventory Microservice to get update about stock.
        InventoryResponse productStock = webClientBuilder.build().get()
                .uri("http://localhost:8080/api/inventory/get-product-byId/" + product.getId())
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(InventoryResponse.class)
                .transform(it->{
                    ReactiveCircuitBreaker rcb = reactiveCircuitBreakerFactory.create("PRODUCT-SERVICE");
                    return rcb.run(it,throwable -> Mono.just(InventoryResponse.builder().build()));
                })
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
                .imageUrl("http://localhost:8080/api/image/download/"+product.getProductImage())
                .productCategory(product.getProductCategory())
                .description(product.getDescription())
                .brand(brandDto)
                .productIsInStock(isInStock)
                .productImages(productImages)
                .subProducts(subProductDtoList)
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
    @Transactional
    @Async
    public void saveSubProduct(SubProductModel val, String token) {
        try {
            SubProduct subProduct = SubProduct.builder()
                    .productId(val.getProductId())
                    .subProductName(val.getSubProductName())
                    .price(val.getPrice())
                    .build();
            Brand brand = brandRepository.findByName(val.getBrandName())
                    .orElseThrow(() -> new
                            SpringProductException("Unable to find Brand : ProductServiceImpl.saveSubProduct"));
            Description description = Description.builder()
                    .data(val.getDescription())
                    .build();
            descriptionRepository.save(description);
            log.info("Description Saved");

            Description productDescription = descriptionRepository.findTopByOrderByIdDesc()
                    .orElseThrow(() -> new SpringProductException("Unable to get description"));
            Product product = Product.builder()
                    .productName(val.getSubProductName())
                    .ratings(0)
                    .noOfRatings(0)
                    .totalRating(0)
                    .text(val.getText())
                    .price(val.getPrice())
                    .discount(val.getDiscount())
                    .newPrice(val.getNewPrice())
                    .productImage(val.getProductImage())
                    .productCategory(val.getProductCategory())
                    .description(productDescription)
                    .brand(brand)
                    .build();
            productRepository.save(product);
            log.info("Product saved Successfully");
            Product lastSavedProduct =productRepository.findTopByOrderByIdDesc();
            log.info("Last Saved Id : {} ",lastSavedProduct.getId());
            // Calling Inventory Microservice to add SubProduct noOfStock.
            webClientBuilder.build().post()
                    .uri("http://localhost:8080/api/inventory/addToInventory/"
                            + val.getSubProductName() + "/" + lastSavedProduct.getId() +"/"+ val.getNoOfStock())
                    .header("Authorization",token)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            brand.setNoOfProducts(brand.getNoOfProducts()+1);
            brandRepository.save(brand);
            subProductRepository.save(subProduct);
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
    public SubProductDto subProductEntityToDto(SubProduct subProduct) {
        return SubProductDto.builder()
                .id(subProduct.getId())
                .subProductName(subProduct.getSubProductName())
                .productId(subProduct.getProductId())
                .price(subProduct.getPrice())
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
