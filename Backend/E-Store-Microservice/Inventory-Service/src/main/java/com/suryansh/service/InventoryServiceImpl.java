package com.suryansh.service;

import com.suryansh.dto.CheckOrderDto;
import com.suryansh.dto.InventoryResponse;
import com.suryansh.entity.*;
import com.suryansh.exception.SpringInventoryException;
import com.suryansh.mapper.InventoryMapper;
import com.suryansh.model.CheckOrderModel;
import com.suryansh.model.InventoryModel;
import com.suryansh.model.OrderDetailModel;
import com.suryansh.repository.InventoryDocumentRepository;
import com.suryansh.repository.SellerDocumentRepository;
import com.suryansh.repository.SellerOrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryDocumentRepository inventoryDocumentRepository;
    private final SellerDocumentRepository sellerDocumentRepository;
    private final InventoryMapper inventoryMapper;
    private final SellerOrderRepository sellerOrderRepository;
    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Override
    @Transactional
    public String addNewSeller(Long userId) {
        Optional<Seller> checkSeller = sellerDocumentRepository.findByUserId(userId);
        if (checkSeller.isPresent()){
            throw new SpringInventoryException("Seller is already present ",
                    "SellerAlreadyPresent",HttpStatus.BAD_REQUEST);
        }
        Seller seller = Seller.builder().userId(userId)
                .username("").feedbackPercentage("").feedbackScore("").build();
        try {
            var newSeller=sellerDocumentRepository.save(seller);
            SellerOrderDocument newOrderDocument = new SellerOrderDocument(newSeller.getId(),0,new ArrayList<>());
            sellerOrderRepository.save(newOrderDocument);
            logger.info("New Seller {} added to inventory ",seller.getId());
            return "Seller is added to inventory ";
        }catch (Exception e){
            logger.error("Unable to save seller to inventory ",e);
            throw new SpringInventoryException("Unable to save Seller in Inventory ",
                    "InventorySellerError",HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public String saveProductInInventory(InventoryModel inventoryModel) {
        Optional<InventoryDocument> inventoryDocument = inventoryDocumentRepository
                .findByProduct_ProductId(inventoryModel.getProduct().getProductId());
        if (inventoryDocument.isPresent()){
            throw new SpringInventoryException("Product is already present in inventory ",
                    "ProductAlreadyPresent",HttpStatus.BAD_REQUEST);
        }
        Seller seller = sellerDocumentRepository.findById(inventoryModel.getSellerId())
                .orElseThrow(()->new SpringInventoryException("Unable to find Seller in inventory"
                        ,"SellerNotFound",HttpStatus.NOT_FOUND));
        inventoryModel.setSellerId(seller.getId());
        InventoryDocument inventory = inventoryMapper.InventoryModelToEntity(inventoryModel);
        try {
            inventoryDocumentRepository.save(inventory);
            logger.info("product {} is saved in inventory of seller {} ",inventoryModel.getProduct().getTitle(),seller.getId());
            return "Product saved successfully ";
        } catch (Exception e) {
            logger.error("Unable to save product {} ",inventory.getProduct().getTitle(),e);
            throw new SpringInventoryException("Unable to Add Product Inventory : Save Product",e.getClass().getTypeName(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public InventoryResponse getInventoryByProductId(Long productId) {
        InventoryDocument document = inventoryDocumentRepository.findByProduct_ProductId(productId)
                .orElseThrow(()->new SpringInventoryException("Unable to find product in inventory "+ productId,
                        "ProductNotFoundInventory",HttpStatus.NOT_FOUND));
        return inventoryMapper.InventoryEntityToDto(document);
    }

    @Override
    public boolean CheckProduct(Long productId, int quantity) {
        InventoryDocument document = inventoryDocumentRepository.findByProduct_ProductId(productId)
                .orElseThrow(()->new SpringInventoryException("Unable to find product in inventory "+ productId,
                        "ProductNotFoundInventory",HttpStatus.NOT_FOUND));
        return document.getEstimatedAvailability().isInStock() && document.getEstimatedAvailability().getTotalStock() >= quantity;
    }

    @Override
    public CheckOrderDto checkAllProductsIsInStock(CheckOrderModel dto) {
        var availabilityList = new ArrayList<CheckOrderDto.Product>();
        boolean stockRes = true;
        for (CheckOrderModel.Product product:dto.products()){
            var inventoryRes = inventoryDocumentRepository.findByProduct_ProductId(product.productId())
                    .orElseThrow(()->new SpringInventoryException("Unable to find inventory of id "+product.productId(),
                            "InventoryNotFound",HttpStatus.NOT_FOUND));
            // If Product is in stock.
            if (inventoryRes.getEstimatedAvailability().isInStock()
                    &&inventoryRes.getEstimatedAvailability().getTotalStock()<= product.requiredAmount()
            ){
                CheckOrderDto.Product p = new CheckOrderDto.Product(product.productId(),inventoryRes.getProduct().getTitle()
                        ,true, inventoryRes.getEstimatedAvailability().getTotalStock());
                availabilityList.add(p);
            }
            // The Product is out of stock.
            else{
                stockRes=false;
                CheckOrderDto.Product p = new CheckOrderDto.Product(product.productId(),inventoryRes.getProduct().getTitle()
                        ,false, inventoryRes.getEstimatedAvailability().getTotalStock());
                availabilityList.add(p);
            }
        }
        return new CheckOrderDto(stockRes,availabilityList.size(),availabilityList);
    }

    @Override
    @Transactional
    public void updateProduct(InventoryModel model, String id) {
        InventoryDocument inventoryDocument = inventoryDocumentRepository.findById(id)
                .orElseThrow(()->new SpringInventoryException("Unable to find inventory of id "+id,
                        "InventoryNotFound",HttpStatus.NOT_FOUND));
        if (inventoryDocument==null)throw new SpringInventoryException("Unable to find inventory of id "+id,
                "InventoryNotFound",HttpStatus.NOT_FOUND);
        inventoryDocument = inventoryMapper.InventoryModelToEntity(model);
        try {
            inventoryDocumentRepository.save(inventoryDocument);
            logger.info("Inventory {} Product is successfully updated ",id);
        } catch (Exception e) {
            logger.error("Unable to update inventory {} ",id,e);
            throw new SpringInventoryException("Unable to Update Inventory" + id,e.getClass().getTypeName(),
                    HttpStatus.BAD_REQUEST);
        }
    }
    @Override
    @Transactional
    public String updateInventory(OrderDetailModel model) {
        for (OrderDetailModel.product m : model.products()) {
            var inventory = inventoryDocumentRepository.findByProduct_ProductId(m.productId())
                    .orElseThrow(() -> new SpringInventoryException("Unable to find inventory of id " + m.productId(),
                            "InventoryNotFound", HttpStatus.NOT_FOUND));
            inventory.getEstimatedAvailability().setTotalStock(inventory.getEstimatedAvailability().getTotalStock() - m.quantity());
            inventory.getEstimatedAvailability().setSoldQuantity(inventory.getEstimatedAvailability().getSoldQuantity() + 1);

            // The Product is out of stock.
            if (inventory.getEstimatedAvailability().getTotalStock() <= 0) {
                inventory.getEstimatedAvailability().setTotalStock(0);
                inventory.getEstimatedAvailability().setInStock(false);
                inventory.getEstimatedAvailability().setAvailabilityStatus(EstimatedAvailability.AvailabilityStatus.OUT_OF_STOCK);
            }

            // Add Order detail for seller.
            SellerOrderDocument sellerOrder = sellerOrderRepository.findById(inventory.getSellerId())
                    .orElseThrow(() -> new SpringInventoryException("Unable to find inventory of id " + m.productId(),
                            "InventoryNotFound", HttpStatus.NOT_FOUND));
            sellerOrder.setTotalOrder(sellerOrder.getTotalOrder()+1);
            OrderDetail newOrderDetail = new OrderDetail(model.userId(), Instant.now(),OrderDetail.status.PLACED,m.productId(),
                    m.price(),m.quantity(),m.price()*m.quantity());
            sellerOrder.getOrderDetails().add(newOrderDetail);
            try {
                inventoryDocumentRepository.save(inventory);
                sellerOrderRepository.save(sellerOrder);
                logger.info("Product {} stock updated !! & order added for seller {} ",m.productId(),inventory.getSellerId());
            }catch (Exception e){
                logger.info("Unable to update inventory after order placed "+e);
                throw new SpringInventoryException("Unable to find inventory of id " + m.productId(),
                        "InventoryNotFound", HttpStatus.NOT_FOUND);
            }
        }
        return "Inventory is successfully updated after order placed!";
    }


}
