package com.suryansh.orderservice.service;

import com.suryansh.orderservice.dto.*;
import com.suryansh.orderservice.entity.Order;
import com.suryansh.orderservice.entity.OrderAddress;
import com.suryansh.orderservice.entity.OrderItems;
import com.suryansh.orderservice.exception.MicroserviceException;
import com.suryansh.orderservice.exception.SpringOrderException;
import com.suryansh.orderservice.model.InventoryModel;
import com.suryansh.orderservice.model.OrderUpdateModel;
import com.suryansh.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final WebClient.Builder webClientBuilder;
    private final OrderRepository orderRepository;

    @Override
    @Async
    @Transactional
    public CompletableFuture<String> placeOrder(String userName, String token) {

        CartDto cart = webClientBuilder.build().get()
                .uri("http://USER-SERVICE/api/cart/getCartByUser/" + userName)
                .header("Authorization", token)
                .retrieve()
                .onStatus(HttpStatus::isError,clientResponse -> Mono.error(new
                        MicroserviceException("Unable to find Cart for username:- "+userName+" for Place Order")))
                .bodyToMono(CartDto.class)
                .block();
        if (cart == null) {
            return CompletableFuture.failedFuture(new
                    SpringOrderException("User Cart is Empty. :placeOrder"));
        }

        List<OrderItems>orderItemsModel = cart.getCartProduct().stream()
                .map(this::cartItemsToOrderItemsEntity)
                .toList();

        // Calling User Microservice to get User By UserName.
        UserDto user = webClientBuilder.build().get()
                .uri("http://USER-SERVICE/api/user/by-username/" + userName)
                .header("Authorization", token)
                .retrieve()
                .onStatus(HttpStatus::isError,clientResponse -> Mono.error(new
                        MicroserviceException("Unable to find User of  username:- "+userName+" for Place Order")))
                .bodyToMono(UserDto.class)
                .block();

        if (user == null) {
            return CompletableFuture.failedFuture(new
                    SpringOrderException("User is not Available. :placeOrder"));
        }

        AddressDto addressDto = webClientBuilder.build().get()
                .uri("http://USER-SERVICE/api/user/getUserAddress/" + user.getUserName())
                .header("Authorization", token)
                .retrieve()
                .onStatus(HttpStatus::isError,clientResponse -> Mono.error(
                        new MicroserviceException("Unable to find Address of user of id:- "+user.getId()+
                                " For Place Order")))
                .bodyToMono(AddressDto.class)
                .block();
        if (addressDto == null) {
            return CompletableFuture.failedFuture(new
                    SpringOrderException("User Address is not Available. :placeOrder"));
        }
        OrderAddress orderAddress = OrderAddress.builder()
                .userId(user.getId())
                .line1(addressDto.getLine1())
                .city(addressDto.getCity())
                .pinCode(addressDto.getPinCode())
                .otherDetails(addressDto.getOtherDetails())
                .build();

        Order order = Order.builder()
                .userId(user.getId())
                .orderDate(Instant.now())
                .lastUpdate(Instant.now())
                .status("Order Placed Successfully")
                .totalItems(cart.getTotalProducts())
                .price(cart.getTotalPrice())
                .isProductDelivered(false)
                .orderItems(orderItemsModel)
                .orderAddress(orderAddress)
                .build();
        try {

            orderRepository.save(order);
            List<InventoryModel> inventoryModels = cart.getCartProduct()
                    .stream()
                    .map(this::CartProductsToInventoryModel)
                    .toList();

            // Calling Inventory Service for Updating Inventory.
            String inventoryResponse = webClientBuilder.build().post()
                    .uri("http://INVENTORY-SERVICE/api/inventory/updateInventoryProducts")
                    .header("Authorization",token)
                    .body(BodyInserters.fromValue(inventoryModels))
                    .retrieve()
                    .onStatus(HttpStatus::isError,clientResponse -> Mono.error(
                            new MicroserviceException("Unable to communicate Inventory Microservice.")
                    ))
                    .bodyToMono(String.class)
                    .block();
            // Calling Cart Service for Clearing Cart for User After Order Placed.
            String cartResponse = webClientBuilder.build().get()
                    .uri("http://USER-SERVICE/api/cart/clearCartForUser/" + userName)
                    .header("Authorization",token)
                    .retrieve()
                    .onStatus(HttpStatus::isError,clientResponse -> Mono.error(new
                            MicroserviceException("Unable to clear cart  for username:-"+userName+" for Place Order")))
                    .bodyToMono(String.class)
                    .block();

            log.info(cartResponse);
            log.info("Inventory Response:- "+inventoryResponse);
            log.info("Order Placed Successfully");
            return CompletableFuture.completedFuture("Order Placed Successfully !!");
        } catch (Exception e) {
            throw new SpringOrderException("Unable to Place Order !! :placeOrder");
        }
    }



    @Override
    public List<OrderDto> getAllOrderByUser(String userName, String token) {
        UserDto user = webClientBuilder.build().get()
                .uri("http://USER-SERVICE/api/user/by-username/" + userName)
                .header("Authorization",token)
                .retrieve()
                .onStatus(HttpStatus::isError,clientResponse -> Mono.error(new
                        MicroserviceException("Unable to communicate userService for getAllOrder")))
                .bodyToMono(UserDto.class)
                .block();
        if (user == null) throw new SpringOrderException("User is not found of name: - "+userName);
        List<Order> orders = orderRepository.findByUserIdOrderByIdDesc(user.getId());
        return orders.stream()
                .map(this::OrderEntityToDto)
                .toList();
    }

    @Override
    public OrderDetails getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new SpringOrderException("Unable to Find Order of Id : " + orderId +
                        " getOrderDetails"));
        OrderAddress orderAddress = order.getOrderAddress();
        List<OrderItemsDto> orderItems = order.getOrderItems().stream()
                .map((item) -> OrderItemsDto.builder()
                        .itemId(item.getItemId())
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
                .orElseThrow(() -> new SpringOrderException("Unable to Find Order By Id : " + orderId+
                        " :getOrderByOrderId "));
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
                .orElseThrow(() -> new SpringOrderException("Unable to Find Order By Id : " + model.getOrderId()+
                        " :updateOrder"));
        order.setIsProductDelivered(model.getIsProductDelivered());
        order.setLastUpdate(Instant.now());
        order.setStatus(model.getStatus());
        orderRepository.save(order);
    }

    /* List Mapping Functions. */

    private OrderItems cartItemsToOrderItemsEntity(CartItems cartItems) {
        if (!cartItems.getIsInStock())throw new SpringOrderException("Sorry Product:- "+cartItems.getProductName()+" " +
                " is out of stock. Please Try after sometime.");
        else
            return OrderItems.builder()
                    .productId(cartItems.getProductId())
                    .productName(cartItems.getProductName())
                    .quantity(cartItems.getNoOfProduct())
                    .price(cartItems.getPrice())
                    .build();
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
