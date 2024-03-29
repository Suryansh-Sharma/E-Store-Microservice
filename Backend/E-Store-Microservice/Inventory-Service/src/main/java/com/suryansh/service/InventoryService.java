package com.suryansh.service;

import com.suryansh.dto.CheckOrderDto;
import com.suryansh.dto.InventoryResponse;
import com.suryansh.model.CheckOrderModel;
import com.suryansh.model.InventoryModel;

public interface InventoryService {
    String addNewSeller(Long userId);

    InventoryResponse getInventoryByProductId(Long productId);

    boolean CheckProduct(Long product, int quantity);

    void updateProduct(InventoryModel model, String id);


    CheckOrderDto checkAllProductsIsInStock(CheckOrderModel dto);

}
