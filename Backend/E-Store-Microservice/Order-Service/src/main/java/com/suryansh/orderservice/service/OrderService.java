package com.suryansh.orderservice.service;

import com.suryansh.orderservice.dto.CartDto;
import com.suryansh.orderservice.dto.OrderDetails;
import com.suryansh.orderservice.dto.OrderDto;
import com.suryansh.orderservice.model.OrderUpdateModel;

import java.util.List;

public interface OrderService {
    void placeOrder(String userName, CartDto cartDto);

    List<OrderDto> getAllOrderByUser(String userName);

    OrderDetails getOrderDetails(Long orderId);

    OrderDto getOrderByOrderId(Long orderId);

    List<OrderDto> getAllPendingOrder();

    void updateOrder(OrderUpdateModel orderUpdateModel);
}
