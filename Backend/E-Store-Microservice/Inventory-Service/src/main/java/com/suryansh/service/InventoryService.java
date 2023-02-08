package com.suryansh.service;

import com.suryansh.dto.InventoryResponse;
import com.suryansh.model.OrderItemsModel;

import java.util.List;

public interface InventoryService {
    void saveProducts(List<OrderItemsModel> model);

    List<InventoryResponse> checkAllProducts(List<OrderItemsModel> models);

    void updateProduct(OrderItemsModel model);

    List<InventoryResponse> findAll();

    void CheckProduct(String product);

    void saveProduct(String productName, Long id, int noOfStock);

    InventoryResponse getProductById(Long productId);

    String updateInventory(List<OrderItemsModel> model);
}
