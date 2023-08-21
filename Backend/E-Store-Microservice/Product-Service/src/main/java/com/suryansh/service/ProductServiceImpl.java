package com.suryansh.service;

import com.suryansh.dto.*;
import com.suryansh.entity.Brand;
import com.suryansh.entity.Product;
import com.suryansh.entity.ProductBelongsTo;
import com.suryansh.exception.SpringProductException;
import com.suryansh.mapper.ProductMapper;
import com.suryansh.model.ElasticSearchProductModel;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
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
    private final KafkaTemplate<String, InventoryModel> kafkaInventoryAddTemplate;
    private final KafkaTemplate<String, RatingAndReviewModel> kafkaAddRatingTemplate;

    @Override
    @Transactional
    @Async
    public CompletableFuture<String> save(ProductModel productModel) {
        Optional<Product> productFromDb = productRepository
                .findByTitle(productModel.getTitle());
        if (productFromDb.isPresent())
            return CompletableFuture.failedFuture(new
                    SpringProductException("Product is already present !!"));
        Brand brand = brandRepository.findByName(productModel.getBrand().getName())
                .orElseThrow(() -> new CompletionException(new SpringProductException("Unable to save product because Brand not found.")));

        Product product = productMapper.ProductModelToEntity(productModel, brand);
        try {
            product = productRepository.save(product);
            InventoryModel inventoryModel = productModel.getInventoryModel();
            InventoryModel.InventoryProductModel productInventoryModel = new InventoryModel.InventoryProductModel();
            productInventoryModel.setProductId(product.getId());
            productInventoryModel.setTitle(product.getTitle());
            inventoryModel.setProduct(productInventoryModel);

            // Send request to inventory for new product through Kafka.
            kafkaInventoryAddTemplate.send("add-new-product-inventory", inventoryModel);

            // Send request to rating-review-service for new product through Kafka.
            RatingAndReviewModel ratingAndReviewModel = new RatingAndReviewModel(product.getId(), product.getTitle()
                    , productModel.getRatingAndReview().getRating(), productModel.getRatingAndReview().getRatingCount()
                    , productModel.getRatingAndReview().getReviewCount());
            kafkaAddRatingTemplate.send("add-new-product-rating", ratingAndReviewModel);

            // Send Product details to Elastic search through kafka.
            ElasticSearchProductModel searchModel = productMapper.convertProductEntityToElasticDoc(product, productModel);
            logger.info("Elastic search model {} ", searchModel);
            logger.info("Product Saved to Product Microservice,Inventory Service,Review Service");
            return CompletableFuture.completedFuture("Product added successfully");
        }catch (Exception e) {
            logger.error("Unable to save product !! " + e);
            throw new SpringProductException("Unable to save Product : ProductServiceImpl.save Catch block " + e);
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
                            , null, 0, 0, null)));
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

    @Override
    public ProductModel testInventory(ProductModel productModel) {
        logger.info("Inventory model {} ", productModel.getInventoryModel());
        InventoryModel inventoryModel = productModel.getInventoryModel();
        InventoryModel.InventoryProductModel productInventoryModel = new InventoryModel.InventoryProductModel();
        productInventoryModel.setProductId(1L);
        productInventoryModel.setTitle("product.getTitle()");
        inventoryModel.setProduct(productInventoryModel);
        logger.info("Inventory model before sending {}", inventoryModel);
        kafkaInventoryAddTemplate.send("add-new-product-inventory", inventoryModel);

        return productModel;
    }

    @Override
    @Transactional
    public String createRelationshipForProductAndRelatedProduct(long parentProductId, long childProductId) {
        // Fetch the parent and child products from the database
        Product parentProduct = productRepository.findById(parentProductId)
                .orElseThrow(() -> new SpringProductException("Parent product not found"));

        Product childProduct = productRepository.findById(childProductId)
                .orElseThrow(() -> new SpringProductException("Child product not found"));


        ProductBelongsTo productBelongsTo = parentProduct.getBelongsTo();
        if (productBelongsTo == null) {
            productBelongsTo = new ProductBelongsTo();
            productBelongsTo.setProducts(new ArrayList<>());
            productBelongsTo = belongsToRepository.save(productBelongsTo); // Save the new ProductBelongsTo
            parentProduct.setBelongsTo(productBelongsTo);
        } else {
            // Check whether parent already has that child.
            for (Product p : productBelongsTo.getProducts()) {
                if (p.getId().equals(childProductId)) {
                    return "Parent already contain this child";
                }
            }
        }

        // Add the child product to the list of related products
        productBelongsTo.getProducts().add(childProduct);
        productBelongsTo.setTotalProducts(productBelongsTo.getProducts().size());

        // Update the parent product in the database
        try {
            productRepository.save(parentProduct);
            logger.info("Successfully created relationship for parent {} and child {} ", parentProductId, childProductId);
            return "Successfully created relationship for parent and child ";
        } catch (Exception e) {
            logger.error("Unable to make product parent child relationship " + e);
            throw new SpringProductException("Unable to make product parent child relationship " + e);
        }
    }

    @Override
    public List<ProductDto> getBelongsProductFromDb(long parentId) {
        ProductBelongsTo productBelongsTo = belongsToRepository.findById(parentId)
                .orElseThrow(()->new SpringProductException("Unable to parent of id "+parentId));

        return productBelongsTo.getProducts().stream()
                .map(productMapper::convertProductEntityToDto)
                .toList();
    }

    @Override
    public ProductPagingDto getRelatedProductByCategory(String categoryTree, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<Product> products = productRepository.findAllRelatedProducts(categoryTree,pageable);
        return new ProductPagingDto(
                pageNo,
                products.getTotalPages(),
                products.get()
                        .map(productMapper::convertProductEntityToDto)
                        .toList(),
                products.getTotalElements()
        );
    }

}
