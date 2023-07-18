package com.suryansh.service;

import com.suryansh.dto.CheckOrderDto;
import com.suryansh.dto.InventoryResponse;
import com.suryansh.model.CheckOrderModel;
import com.suryansh.model.InventoryModel;
import com.suryansh.model.OrderDetailModel;

public interface InventoryService {
    String addNewSeller(Long userId);

    String saveProductInInventory(InventoryModel inventoryModel);

    InventoryResponse getInventoryByProductId(Long productId);

    boolean CheckProduct(Long product, int quantity);

    void updateProduct(InventoryModel model, String id);

    String updateInventory(OrderDetailModel model);

    CheckOrderDto checkAllProductsIsInStock(CheckOrderModel dto);

}
