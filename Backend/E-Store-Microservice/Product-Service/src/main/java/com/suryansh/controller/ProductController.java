package com.suryansh.controller;

import com.suryansh.dto.ProductDto;
import com.suryansh.dto.ProductFullDto;
import com.suryansh.dto.ProductPagingDto;
import com.suryansh.model.ProductModel;
import com.suryansh.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public CompletableFuture<String> saveProduct(@RequestBody @Valid ProductModel productModel) {
        return productService.save(productModel);
    }
    @PostMapping("/make-relationship/{parent_product_id}/{child_product_id}")
    public String makeProductRelationWithRelatedProduct(@PathVariable long parent_product_id,@PathVariable long child_product_id){
        return productService.createRelationshipForProductAndRelatedProduct(parent_product_id,child_product_id);
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

    @GetMapping("/test")
    public ProductModel productModel(@RequestBody @Valid ProductModel productModel){
        return productService.testInventory(productModel);
    }
    @GetMapping("/belongs-products/{parent_id}")
    public List<ProductDto> getAllBelongsProduct(@PathVariable long parent_id){
        return productService.getBelongsProductFromDb(parent_id);
    }
    @GetMapping("/similar-category-product/{category_tree}")
    public ProductPagingDto getSameCategoryProducts(@PathVariable String category_tree
    ,@RequestParam(required = false,defaultValue = "0") int page_no
    ,@RequestParam(required = false,defaultValue = "6")int page_size){
        return productService.getRelatedProductByCategory(category_tree,page_no,page_size);
    }
}
