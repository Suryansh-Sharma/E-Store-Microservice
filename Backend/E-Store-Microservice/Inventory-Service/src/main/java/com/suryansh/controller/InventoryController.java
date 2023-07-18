package com.suryansh.controller;

import com.suryansh.dto.CheckOrderDto;
import com.suryansh.dto.InventoryResponse;
import com.suryansh.model.CheckOrderModel;
import com.suryansh.model.InventoryModel;
import com.suryansh.model.OrderDetailModel;
import com.suryansh.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory/")
@CrossOrigin
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/add-seller/{userId}")
    public String addSellerToInventory(@PathVariable Long userId){
        return inventoryService.addNewSeller(userId);
    }

    @GetMapping("/by-product-id/{id}")
    public InventoryResponse getInventoryResponse(@PathVariable Long id) {
        return inventoryService.getInventoryByProductId(id);
    }
    @PostMapping("/single-product")
    public String addSingleProductToInventory(@RequestBody @Valid InventoryModel inventoryModel) {
        return inventoryService.saveProductInInventory(inventoryModel);
    }

    @GetMapping("/check/availability/{product}/quantity/{quantity}")
    public boolean checkProductIsInStock(@PathVariable Long product,@PathVariable int quantity) {
       return inventoryService.CheckProduct(product,quantity);
    }
    @GetMapping("/check/list/availability")
    public CheckOrderDto checkAllProductsAreInStock(@RequestBody @Valid CheckOrderModel model){
        return inventoryService.checkAllProductsIsInStock(model);
    }

    @PutMapping("/update/{id}")
    public void updateInventoryProduct(@RequestBody InventoryModel model,@PathVariable String id) {
        inventoryService.updateProduct(model,id);
    }

    @PostMapping("/update-after-order-placed")
    public String updateInventoryAfterOrder(@RequestBody @Valid OrderDetailModel model) {
        return inventoryService.updateInventory(model);
    }

}
