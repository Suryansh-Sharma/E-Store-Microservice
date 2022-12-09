package com.suryansh.controller;

import com.suryansh.dto.InventoryResponse;
import com.suryansh.model.InventoryModel;
import com.suryansh.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@CrossOrigin
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping()
    public List<InventoryResponse> getAllProducts() {
        return inventoryService.findAll();
    }

    @PostMapping()
    public ResponseEntity<Void> addProductToInventory(@RequestBody List<InventoryModel> model) {
        try {
            inventoryService.saveProducts(model);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/addToInventory/{productName}/{id}/{noOfStock}")
    @Async
    public CompletableFuture<ResponseEntity<String>> addSingleProductToInventory(@PathVariable String productName
            , @PathVariable Long id, @PathVariable int noOfStock) {
        try {
            inventoryService.saveProduct(productName, id, noOfStock);
            return CompletableFuture.completedFuture(new ResponseEntity<>("Product Saved to Inventory Successfully",
                    HttpStatus.OK));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(new ResponseEntity<>("Unable to Save Product to Inventory",
                    HttpStatus.CONFLICT));
        }
    }

    @GetMapping("/check-product/{product}")
    public ResponseEntity<Boolean> checkProductIsInStock(@PathVariable String product) {
        try {
            inventoryService.CheckProduct(product);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @GetMapping("/get-product-byId/{productId}")
    public ResponseEntity<InventoryResponse> checkProductIsInStockById(@PathVariable Long productId) {
        try {
            InventoryResponse product = inventoryService.getProductById(productId);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/check-products")
    public List<InventoryResponse> checkProductsIsInStock(@RequestBody List<InventoryModel> models) {
        return inventoryService.checkAllProducts(models);
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<Void> updateInventoryProduct(@RequestBody InventoryModel model) {
        try {
            inventoryService.updateProduct(model);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/updateInventoryProducts")
    public void updateInventoryProducts(@RequestBody List<InventoryModel> model) {
        inventoryService.updateInventory(model);
    }
}
