package com.suryansh.orderservice.mapper;

import com.suryansh.orderservice.dto.*;
import com.suryansh.orderservice.entity.Order;
import com.suryansh.orderservice.entity.OrderAddress;
import com.suryansh.orderservice.entity.OrderItem;
import com.suryansh.orderservice.model.OrderInventoryModel;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class OrderServiceMapping {
    public OrderAddress mapAddressDtoToEntity(UserProfileDto user){
        return OrderAddress.builder()
                .userId(user.id())
                .line1(user.address().line1())
                .city(user.address().city())
                .pinCode(user.address().pinCode())
                .otherDetails(user.address().otherDetails())
                .build();
    }
    public Order mapOrderModelToEntity(CartDto cartDto, Long userId, OrderAddress orderAddress, List<OrderItem> orderItems){
        return Order.builder()
                .userId(userId)
                .orderDate(Instant.now())
                .lastUpdate(Instant.now())
                .status("Order Placed Successfully")
                .totalItems(cartDto.getTotalProducts())
                .price(cartDto.getTotalPrice())
                .isProductDelivered(false)
                .orderItems(orderItems)
                .orderAddress(orderAddress)
                .build();
    }
    public OrderItem cartItemsToOrderItemEntity(CartItem cartItems) {
        return OrderItem.builder()
                .productId(cartItems.getProductId())
                .productName(cartItems.getProductName())
                .quantity(cartItems.getNoOfProduct())
                .price(cartItems.getPrice())
                .build();
    }
    public OrderInventoryModel orderDetailToInventory(List<OrderItem> cartItem, long userId) {
        List<OrderInventoryModel.product> products = cartItem.stream()
                .map(item-> new OrderInventoryModel.product(item.getProductId(), item.getPrice(), item.getQuantity())
                        )
                .toList();
        return new OrderInventoryModel(userId,products);
    }


    public OrderDto OrderEntityToDto(Order order) {
        PrettyTime p = new PrettyTime();
        return new OrderDto(
                order.getId(),
                order.getUserId(),
                p.format(order.getOrderDate()),
                p.format(order.getLastUpdate()),
                order.getStatus(),
                order.getTotalItems(),
                order.getPrice(),
                order.getIsProductDelivered()
        );
    }
    public OrderDetailDto orderEntityToDetail(Order order){
        OrderAddress address = order.getOrderAddress();
        List<OrderItemDto>itemsDto = order.getOrderItems().stream()
                .map(this::orderItemEntityToDto)
                .toList();
        return new OrderDetailDto(
                order.getId(),
                order.getStatus(),
                order.getTotalItems(),
                order.getPrice(),
                address.getLine1(),
                address.getCity(),
                address.getPinCode(),
                address.getOtherDetails(),
                order.getIsProductDelivered(),
                itemsDto
        );
    }
    public OrderItemDto orderItemEntityToDto(OrderItem item){
        return new OrderItemDto(
                item.getItemId(),
                item.getOrderId(),
                item.getProductId(),
                item.getProductName(),
                item.getQuantity(),
                item.getPrice()
        );
    }
}
