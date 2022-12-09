package com.suryansh.orderservice.controller;

import com.suryansh.orderservice.dto.CartDto;
import com.suryansh.orderservice.dto.OrderDetails;
import com.suryansh.orderservice.dto.OrderDto;
import com.suryansh.orderservice.model.OrderUpdateModel;
import com.suryansh.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/order")
@CrossOrigin("*")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/placeOrder/{userName}")
    @Async
    public void placeOrder(@RequestBody CartDto cartDto, @PathVariable String userName) {
        orderService.placeOrder(userName, cartDto);
    }

    @GetMapping("/getOrder-byUser/{userName}")
    public List<OrderDto> getOrderByUser(@PathVariable String userName) {
        return orderService.getAllOrderByUser(userName);
    }

    @GetMapping("/getOrder-byId/{orderId}")
    public OrderDto getOrderByOrderId(@PathVariable Long orderId) {
        return orderService.getOrderByOrderId(orderId);
    }

    @GetMapping("/getOrderDetails/{orderId}")
    public OrderDetails getOrderDetails(@PathVariable Long orderId) {
        return orderService.getOrderDetails(orderId);
    }

    @GetMapping("/getAllPendingOrder")
    public List<OrderDto> getAllPendingOrder() {
        return orderService.getAllPendingOrder();
    }

    @PutMapping("/updateProduct")
    public void updateOrder(@RequestBody OrderUpdateModel orderUpdateModel) {
        orderService.updateOrder(orderUpdateModel);
    }
}
