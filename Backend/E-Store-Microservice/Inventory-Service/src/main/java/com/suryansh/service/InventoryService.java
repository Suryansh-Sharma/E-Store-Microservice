package com.suryansh.service;

import com.suryansh.dto.InventoryResponse;
import com.suryansh.model.InventoryModel;

import java.util.List;

public interface InventoryService {
    void saveProducts(List<InventoryModel> model);

    List<InventoryResponse> checkAllProducts(List<InventoryModel> models);

    void updateProduct(InventoryModel model);

    List<InventoryResponse> findAll();

    void CheckProduct(String product);

    void saveProduct(String productName, Long id, int noOfStock);

    InventoryResponse getProductById(Long productId);

    void updateInventory(List<InventoryModel> model);
}
