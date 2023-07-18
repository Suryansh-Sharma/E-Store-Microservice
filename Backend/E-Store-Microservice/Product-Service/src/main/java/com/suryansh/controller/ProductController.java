package com.suryansh.controller;

import com.suryansh.dto.ProductDto;
import com.suryansh.dto.ProductFullDto;
import com.suryansh.model.ProductModel;
import com.suryansh.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/products/")
@CrossOrigin("*")
public class ProductController {
    private final ProductService productService;
    ProductController(ProductService productService){
        this.productService=productService;
    }


    @PostMapping("/add-new-product")
//    @PreAuthorize("hasAuthority('Admin')")
    @Async
    public CompletableFuture<String> saveProduct(@RequestBody @Valid ProductModel productModel,
                                                 @RequestHeader(name = "Authorization") String token) {
        return productService.save(productModel,token);
    }

    @GetMapping("full-view/by-name/{name}")
    @Async
    public CompletableFuture<ProductFullDto> fullViewByName(@PathVariable String name) {
        return productService.fullViewByName(name);
    }

    @GetMapping("full-view/by-id/{id}")
    @Async
    public CompletableFuture<ProductFullDto> fullViewById(@PathVariable Long id) {
        return productService.fullViewById(id);
    }

    @GetMapping("by-name/{name}")
    public ProductDto getProductByName(@PathVariable String name) {
        return productService.getProductByName(name);
    }

    @GetMapping("by-id/{id}")
    public ProductDto getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

}
