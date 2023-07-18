package com.suryansh.service;

import com.suryansh.dto.InventoryResponse;
import com.suryansh.dto.ProductDto;
import com.suryansh.dto.ProductFullDto;
import com.suryansh.dto.ProductRatingDto;
import com.suryansh.entity.Brand;
import com.suryansh.entity.Product;
import com.suryansh.entity.ProductBelongsTo;
import com.suryansh.exception.SpringProductException;
import com.suryansh.mapper.ProductMapper;
import com.suryansh.model.InventoryModel;
import com.suryansh.model.ProductModel;
import com.suryansh.model.RatingAndReviewModel;
import com.suryansh.repository.BrandRepository;
import com.suryansh.repository.ProductBelongsToRepository;
import com.suryansh.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final WebClient.Builder webClientBuilder;
    private final ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;
    private final ProductBelongsToRepository belongsToRepository;
    private final ProductMapper productMapper;
    private final KafkaTemplate<String,InventoryModel>kafkaInventoryTemplate;
    private final KafkaTemplate<String, RatingAndReviewModel>kafkaRatingReviewTemplate;

    @Override
    @Transactional
    @Async
    public CompletableFuture<String> save(ProductModel productModel, String token) {
        Optional<Product> productFromDb = productRepository
                .findByTitle(productModel.getTitle());
        if (productFromDb.isPresent())
            return CompletableFuture.failedFuture(new
                    SpringProductException("Product is already present !!"));
        Brand brand = brandRepository.findByName(productModel.getBrand().getName())
                .orElseThrow(() -> new CompletionException(new SpringProductException("Unable to save product because Brand not found.")));
        ProductBelongsTo belongsTo ;
        if (productModel.getProductBelongsTo().isHaveParent()) {
            belongsTo = belongsToRepository.findById(productModel.getProductBelongsTo().getId())
                    .orElseThrow(() -> new CompletionException(new SpringProductException("Unable to save product because Parent not found.")));
        }else belongsTo=null;
        Product product = productMapper.ProductModelToEntity(productModel, brand, belongsTo);
        try {
            product = productRepository.save(product);
            InventoryModel inventoryModel = productModel.getInventoryModel();
            InventoryModel.InventoryProductModel productInventoryModel = new InventoryModel.InventoryProductModel();
            productInventoryModel.setProductId(product.getId());
            productInventoryModel.setTitle(product.getTitle());
            inventoryModel.setProduct(productInventoryModel);

            // Send request to inventory for new product through Kafka.
            kafkaInventoryTemplate.send("add-new-product-inventory", inventoryModel);

            // Send request to rating-review-service for new product through Kafka.
            RatingAndReviewModel ratingAndReviewModel = new RatingAndReviewModel(product.getId(), product.getTitle());
            kafkaRatingReviewTemplate.send("add-new-product-rating",ratingAndReviewModel);

            // Send Product details to Elastic search through kafka.

            logger.info("Product Saved to Product Microservice,Inventory Service,Review Service");
            return CompletableFuture.completedFuture("Product added successfully");
        }catch (Exception e) {
            logger.error("Unable to save product !! " + e);
            throw new SpringProductException("Unable to save Product : ProductServiceImpl.save Catch block");
        }
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<ProductFullDto> fullViewByName(String name) {
        Optional<Product> product = productRepository.findByTitle(name);
        if (product.isEmpty()) {
            return CompletableFuture.failedFuture(new SpringProductException("Unable to find product " + name));
        }
        return getProductFullDetails(product.get());
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<ProductFullDto> fullViewById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty())
            return CompletableFuture.failedFuture(new SpringProductException("Unable to find product of id " + id));
        return getProductFullDetails(product.get());
    }

    @Transactional
    public CompletableFuture<ProductFullDto> getProductFullDetails(Product product) {

        // Calling Inventory Microservice to get update about stock.
        InventoryResponse productStock = webClientBuilder.build().get()
                .uri("https://INVENTORY-SERVICE/api/inventory/by-product-id/" + product.getId())
                .retrieve()
                .bodyToMono(InventoryResponse.class)
                .transform(it -> {
                    ReactiveCircuitBreaker rcb = reactiveCircuitBreakerFactory.create("PRODUCT-SERVICE");
                    return rcb.run(it, throwable -> Mono.just(new InventoryResponse()));
                })
                .block();

        // Calling Review Service to get review for product.
        ProductRatingDto productRatingDto = webClientBuilder.build().get()
                .uri("https://REVIEW-SERVICE/api/rating/product-id/"+product.getId())
                .retrieve()
                .bodyToMono(ProductRatingDto.class)
                .transform(it->{
                    ReactiveCircuitBreaker rcb = reactiveCircuitBreakerFactory.create("REVIEW-SERVICE");
                    return rcb.run(it, throwable -> Mono.just(new ProductRatingDto(null,null
                            ,null,0,null)));
                })
                .block();
        ProductFullDto productFullDto = productMapper
                .convertProductFullEntityToDto(product,productStock,productRatingDto);
        return CompletableFuture.completedFuture(productFullDto);
    }

    @Override
    public ProductDto getProductByName(String name) {
        Product product = productRepository.findByTitle(name)
                .orElseThrow(() -> new SpringProductException("Unable to find Product :ProductService.findByName"));
        return productMapper.convertProductEntityToDto(product);
    }


    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new SpringProductException("Unable to find Product of id:-  " +
                        id + ":ProductService.findById"));
        return productMapper.convertProductEntityToDto(product);
    }

}
