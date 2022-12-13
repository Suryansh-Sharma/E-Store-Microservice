package com.suryansh.orderservice.service;

import com.suryansh.orderservice.dto.*;
import com.suryansh.orderservice.entity.Order;
import com.suryansh.orderservice.entity.OrderAddress;
import com.suryansh.orderservice.entity.OrderItems;
import com.suryansh.orderservice.exception.exception.SpringOrderException;
import com.suryansh.orderservice.model.InventoryModel;
import com.suryansh.orderservice.model.OrderUpdateModel;
import com.suryansh.orderservice.repository.OrderAddressRepository;
import com.suryansh.orderservice.repository.OrderItemsRepository;
import com.suryansh.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderItemsRepository orderItemsRepository;
    private final OrderAddressRepository orderAddressRepository;
    private final WebClient.Builder webClientBuilder;
    private final OrderRepository orderRepository;

    @Override
    @Async
    @Transactional
    public void placeOrder(String userName, CartDto cart, String token) {
        RestTemplate restTemplate = new RestTemplate();
        // Calling User Microservice to get User By UserName.
        UserDto user = webClientBuilder.build().get()
                .uri("http://geekyprogrammer:8080/api/user/by-userName/" + userName)
                .header("Authorization",token)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
        assert user != null;
        AddressDto addressDto = webClientBuilder.build().get()
                .uri("http://geekyprogrammer:8080/api/user/getUserAddressById/" + user.getId())
                .header("Authorization",token)
                .retrieve()
                .bodyToMono(AddressDto.class)
                .block();
        try {

            Order order = Order.builder()
                    .userId(user.getId())
                    .orderDate(Instant.now())
                    .lastUpdate(Instant.now())
                    .status("Submitted To Seller")
                    .totalItems(cart.getTotalProducts())
                    .price(cart.getTotalPrice())
                    .isProductDelivered(false)
                    .build();
            orderRepository.save(order);//
            Order o2 = orderRepository.findTopByOrderByIdDesc();
            assert addressDto != null;
            OrderAddress orderAddress = OrderAddress.builder()
                    .orderId(o2.getId())
                    .userId(user.getId())
                    .line1(addressDto.getLine1())
                    .city(addressDto.getCity())
                    .pinCode(addressDto.getPinCode())
                    .otherDetails(addressDto.getOtherDetails())
                    .build();

            List<InventoryModel> inventoryModels = cart.getCartProduct()
                    .stream()
                    .map(this::CartProductsToInventoryModel)
                    .toList();

            orderAddressRepository.save(orderAddress);
            cart.getCartProduct().forEach((item) -> {
                OrderItems orderItems = OrderItems.builder()
                        .orderId(o2.getId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .quantity(item.getNoOfProduct())
                        .price(item.getPrice())
                        .build();
                orderItemsRepository.save(orderItems);
            });
            // Calling Inventory Service for Updating Inventory.
            webClientBuilder.build().post()
                            .uri("http://geekyprogrammer:8080/api/inventory/updateInventoryProducts")
                            .header("Authorization",token)
                            .body(BodyInserters.fromValue(inventoryModels));
            log.info("Inventory Updated Successfully");
            // Calling Cart Service for Clearing Cart for User After Order Placed.
            String cartResponse = webClientBuilder.build().get()
                    .uri("http://geekyprogrammer:8080/api/cart/clearCartForUser/" + userName)
                    .header("Authorization",token)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info(cartResponse);
            log.info("Order Placed Successfully");
        } catch (Exception e) {
            throw new SpringOrderException("Unable to Place Order !!");
        }
    }
    @Override
    public List<OrderDto> getAllOrderByUser(String userName, String token) {
        UserDto user = webClientBuilder.build().get()
                .uri("http://geekyprogrammer:8080/api/user/by-userName/" + userName)
                .header("Authorization",token)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToMono(UserDto.class)
                .block();
        if (user == null) return null;
        List<Order> orders = orderRepository.findByUserIdOrderByIdDesc(user.getId());
        return orders.stream()
                .map(this::OrderEntityToDto)
                .toList();
    }

    @Override
    public OrderDetails getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new SpringOrderException("Unable to Find Order of Id : " + orderId));
        OrderAddress orderAddress = orderAddressRepository.findByOrderId(orderId);
        List<OrderItemsDto> orderItems = order.getOrderItems().stream()
                .map((item) -> OrderItemsDto.builder()
                        .itemId(item.getItemId())
                        .orderId(item.getOrderId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .toList();
        return OrderDetails.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .price(order.getPrice())
                .totalItems(order.getTotalItems())
                .orderItems(orderItems)
                .line1(orderAddress.getLine1())
                .city(orderAddress.getCity())
                .pinCode(orderAddress.getPinCode())
                .otherDetails(orderAddress.getOtherDetails())
                .isProductDelivered(order.getIsProductDelivered())
                .build();
    }

    @Override
    public OrderDto getOrderByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new SpringOrderException("Unable to Find Order By Id : " + orderId));
        return OrderEntityToDto(order);
    }

    @Override
    public List<OrderDto> getAllPendingOrder() {
        return orderRepository.findByIsProductDelivered(false)
                .stream().map(this::OrderEntityToDto)
                .toList();
    }

    @Override
    @Transactional
    public void updateOrder(OrderUpdateModel model) {
        Order order = orderRepository.findById(model.getOrderId())
                .orElseThrow(() -> new SpringOrderException("Unable to Find Order By Id : " + model.getOrderId()));
        order.setIsProductDelivered(model.getIsProductDelivered());
        order.setLastUpdate(Instant.now());
        order.setStatus(model.getStatus());
        orderRepository.save(order);
    }

    private OrderDto OrderEntityToDto(Order order) {
        PrettyTime p = new PrettyTime();
        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .orderDate(p.format(order.getOrderDate()))
                .lastUpdate(p.format(order.getLastUpdate()))
                .status(order.getStatus())
                .totalItems(order.getTotalItems())
                .price(order.getPrice())
                .isProductDelivered(order.getIsProductDelivered())
                .build();
    }

    private InventoryModel CartProductsToInventoryModel(CartItems cartItem) {
        return InventoryModel.builder()
                .productName(cartItem.getProductName())
                .productId(cartItem.getProductId())
                .noOfStock(cartItem.getNoOfProduct())
                .totalSold(cartItem.getNoOfProduct())
                .build();
    }

}
