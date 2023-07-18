package com.suryansh.orderservice.controller;

import com.suryansh.orderservice.dto.OrderDetailDto;
import com.suryansh.orderservice.dto.OrderDto;
import com.suryansh.orderservice.model.OrderUpdateModel;
import com.suryansh.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/api/order")
@CrossOrigin("*")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Order place endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/place/{userName}")
    public CompletableFuture<String> placeOrder(@PathVariable String userName,
                                   @RequestHeader(name = "Authorization") String token) {
        return orderService.placeOrder(userName,token);
    }

    @GetMapping("/by-user/{userName}")
    @Operation(summary = "Get User endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public List<OrderDto> getOrderByUser(@PathVariable String userName,
                                         @RequestHeader(name = "Authorization") String token) {
        return orderService.getAllOrderByUser(userName,token);
    }

    @GetMapping("/by-id/{orderId}")
    public OrderDto getOrderByOrderId(@PathVariable Long orderId) {
        return orderService.getOrderByOrderId(orderId);
    }

    @GetMapping("/full-detail/{orderId}")
    public OrderDetailDto getOrderDetails(@PathVariable Long orderId) {
        return orderService.getOrderDetails(orderId);
    }

    @GetMapping("/all-pending")
    public List<OrderDto> getAllPendingOrder() {
        return orderService.getAllPendingOrder();
    }

    @PutMapping("/update")
    @Operation(summary = "Update Order endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public void updateOrder(@RequestBody OrderUpdateModel orderUpdateModel) {
        orderService.updateOrder(orderUpdateModel);
    }
    @PostMapping("/dummy-inventory-detail-kafka")
    public void sendDummyMail(){
        orderService.sendDummyInventoryDetail();
    }
    @PostMapping("/dummy-clear-cart-kafka")
    public void sendFakeKafkaClearCart(){
        orderService.sendFakeClearCart();
    }
}
