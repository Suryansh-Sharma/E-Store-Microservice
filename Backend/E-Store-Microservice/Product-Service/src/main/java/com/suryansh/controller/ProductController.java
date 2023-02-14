package com.suryansh.controller;

import com.suryansh.dto.NavSearchDto;
import com.suryansh.dto.ProductDto;
import com.suryansh.dto.ProductPagingDto;
import com.suryansh.model.ProductModel;
import com.suryansh.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/products/")
@AllArgsConstructor
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/testProductController")
    public String testProductController() {
        return "<h1>Product Controller Working Properly. !!<h1>";
    }

    @PostMapping("/save")
//    @PreAuthorize("hasAuthority('Admin')")
    @Async
    public CompletableFuture<String> saveProduct(@RequestBody @Valid ProductModel productModel,
                                                 @RequestHeader(name = "Authorization") String token) {
        return productService.save(productModel,token);
    }

    @GetMapping("fullView-by-name/{name}")
    @Async
    public CompletableFuture<ProductDto> fullViewByName(@PathVariable String name) {
        return CompletableFuture.completedFuture(productService.fullViewByName(name));
    }
    @GetMapping("/fullView-by-id/{id}")
    @Async
    public CompletableFuture<ProductDto> fullViewById(@PathVariable Long id) {
        return CompletableFuture.completedFuture(productService.fullViewById(id));
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
    @GetMapping("by-category/{category}")
    public ProductPagingDto getProductsByCategory(@PathVariable String category,
                                                  @RequestParam(name = "pageNo",defaultValue = "0",required = false) int page) {
        Pageable pageable=
                PageRequest.of(page,6);
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
    @GetMapping("/by-nameLike/{productName}")
    public ProductPagingDto getAllNameLike(@PathVariable String productName,
                                           @RequestParam(value = "pageNo",required = false,defaultValue = "0")int page){
        Pageable pageable = PageRequest.of(page,6);
        return productService.findByProductNameLike(productName,pageable);
    }
    @GetMapping("/navSearch/{productName}")
    public List<NavSearchDto> getProductNameAndId(@PathVariable String productName) {
        return productService.findProductNameAndId(productName);
    }

    @PostMapping("/addRatingForProduct/{id}/{rating}")
    public void addRatingForProduct(@PathVariable Long id, @PathVariable int rating) {
        productService.addRatingForProduct(id, rating);
    }
}
