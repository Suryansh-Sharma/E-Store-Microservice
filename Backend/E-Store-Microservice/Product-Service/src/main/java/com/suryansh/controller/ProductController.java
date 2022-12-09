package com.suryansh.controller;

import com.suryansh.dto.ProductDto;
import com.suryansh.dto.ProductPagingDto;
import com.suryansh.entity.Product;
import com.suryansh.exception.SpringProductException;
import com.suryansh.model.ProductModel;
import com.suryansh.model.SubProductModel;
import com.suryansh.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public String testProductController() {
        return "<h1>Product Controller Working Properly. !!<h1>";
    }

    @PostMapping("/save")
    @Async
    public CompletableFuture<String> saveProduct(@RequestBody ProductModel productModel) {
        productService.save(productModel);
        return CompletableFuture.completedFuture("Product saved successfully :Controller");
    }

    @GetMapping("/fullView-by-name/{name}")
    public ResponseEntity<ProductDto> fullViewByName(@PathVariable String name) {
        try {
            return new ResponseEntity<>(productService.fullViewByName(name)
                    , HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null
                    , HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("by-name/{name}")
    public ResponseEntity<ProductDto> getProductByName(@PathVariable String name) {
        try {
            return new ResponseEntity<>(productService.getProductByName(name)
                    , HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null
                    , HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("by-category/{category}/{page}")
    public ProductPagingDto getProductsByCategory(@PathVariable String category,
                                                                  @PathVariable int page) {
        Pageable pageable=
                PageRequest.of(page,2);
        return productService.getProductByCategory(category,pageable);
    }
    @GetMapping("by-id/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(productService.getProductById(id)
                    , HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null
                    , HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/save-subProduct")
    @Async
    public void saveSubProduct(@RequestBody SubProductModel Model) {
        productService.saveSubProduct(Model);
    }
    @GetMapping("/by-nameLike/{productName}/{page}")
    public List<ProductDto> getAllNameLike(@PathVariable String productName, @PathVariable int page){
        PageRequest pr = PageRequest.of(page,2);
        return productService.findByProductNameLike(productName,pr);
    }
}
