package com.suryansh.orderservice.service;

import com.suryansh.orderservice.dto.OrderDetailDto;
import com.suryansh.orderservice.dto.OrderDto;
import com.suryansh.orderservice.model.OrderUpdateModel;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OrderService {
    CompletableFuture<String> placeOrder(String userName, String token);

    List<OrderDto> getAllOrderByUser(String userName, String token);

    OrderDetailDto getOrderDetails(Long orderId);

    OrderDto getOrderByOrderId(Long orderId);

    List<OrderDto> getAllPendingOrder();

    void updateOrder(OrderUpdateModel orderUpdateModel);

    void sendDummyInventoryDetail();

    void sendFakeClearCart();
}
