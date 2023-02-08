package com.suryansh.service;

import com.suryansh.dto.InventoryResponse;
import com.suryansh.entity.Inventory;
import com.suryansh.exception.SpringInventoryException;
import com.suryansh.model.OrderItemsModel;
import com.suryansh.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public void saveProducts(List<OrderItemsModel> model) {
        List<Inventory> inventory = model.stream()
                .map(this::inventoryModelToEntity)
                .toList();
        try {
            inventoryRepository.saveAll(inventory);
        } catch (Exception e) {
            throw new SpringInventoryException("Unable to Add Products to Repository");
        }
    }

    @Override
    @Transactional
    public List<InventoryResponse> checkAllProducts(List<OrderItemsModel> models) {
        List<InventoryResponse> result = new ArrayList<>();
        for (OrderItemsModel val : models) {
            Inventory inventory = inventoryRepository.findByProductName(val.getProductName())
                    .orElseThrow(() -> new SpringInventoryException("Unable to find product : " +
                            val.getProductName()));
            InventoryResponse response = new InventoryResponse();
            response.setProductName(inventory.getProductName());
            response.setNoOfStock(inventory.getNoOfStock());
            response.setTotalSold(inventory.getTotalSold());
            response.setIsInStock(inventory.getNoOfStock() > 0);
            result.add(response);
        }
        return result;
    }

    @Override
    @Transactional
    public void updateProduct(OrderItemsModel model) {
        Inventory inventory = inventoryRepository.findByProductName(model.getProductName())
                .orElseThrow(() -> new SpringInventoryException("Unable to find product for Stock Update : "));
        inventory.setNoOfStock(model.getNoOfStock());
        inventory.setLastStockUpdate(Instant.now());
        try {
            inventoryRepository.save(inventory);
        } catch (Exception e) {
            throw new SpringInventoryException("Unable to Update Product" + model.getProductName());
        }
    }

    @Override
    public List<InventoryResponse> findAll() {
        return inventoryRepository.findAll()
                .stream()
                .map(this::entityToModel)
                .toList();
    }

    @Override
    public void CheckProduct(String product) {
        Inventory inventory = inventoryRepository.findByProductName(product)
                .orElseThrow(() -> new SpringInventoryException("Product not Found : " + product));
        if (inventory.getNoOfStock() <= 0) throw new SpringInventoryException("Product is OutOfStock");
    }

    @Override
    @Transactional
    @Async
    public void saveProduct(String productName, Long id, int noOfStock) {
        log.info("productName: {} , productId: {}, noOfStock {} ",productName,id,noOfStock);
        Inventory inventory = Inventory.builder()
                .productId(id)
                .productName(productName)
                .noOfStock(noOfStock)
                .totalSold(0)
                .lastStockUpdate(Instant.now())
                .build();
        try {
            inventoryRepository.save(inventory);
        } catch (Exception e) {
            throw new SpringInventoryException("Unable to Add Product Inventory : Save Product");
        }
    }

    @Override
    public InventoryResponse getProductById(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new SpringInventoryException("Product not Found of Id: " + productId));
        return entityToModel(inventory);
    }

    @Override
    @Transactional
    public String updateInventory(List<OrderItemsModel> model) {
        for (OrderItemsModel val : model) {
            Inventory inventory = inventoryRepository.findByProductId(val.getProductId())
                    .orElseThrow(() -> new SpringInventoryException("Unable to find Inventory for : " + val));
            inventory.setNoOfStock(inventory.getNoOfStock() - val.getNoOfStock());
            inventory.setTotalSold(val.getTotalSold() + inventory.getTotalSold());
            inventory.setLastStockUpdate(Instant.now());
            try {
                inventoryRepository.save(inventory);
                log.info("Inventory Updated ");
            } catch (Exception e) {
                throw new SpringInventoryException("Unable to Update Inventory for : " + val);
            }
        }
        return "Inventory Updated Successfully";
    }

    private InventoryResponse entityToModel(Inventory inventory) {
        return InventoryResponse.builder()
                .productName(inventory.getProductName())
                .productId(inventory.getProductId())
                .isInStock(inventory.getNoOfStock() > 0)
                .noOfStock(inventory.getNoOfStock())
                .totalSold(inventory.getTotalSold())
                .build();
    }

    private Inventory inventoryModelToEntity(OrderItemsModel model) {
        return Inventory.builder()
                .productName(model.getProductName())
                .productId(model.getProductId())
                .noOfStock(model.getNoOfStock())
                .totalSold(0)
                .lastStockUpdate(Instant.now())
                .build();
    }
}
