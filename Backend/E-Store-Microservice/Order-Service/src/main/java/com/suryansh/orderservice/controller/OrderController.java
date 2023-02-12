package com.suryansh.orderservice.controller;

import com.suryansh.orderservice.dto.OrderDetails;
import com.suryansh.orderservice.dto.OrderDto;
import com.suryansh.orderservice.model.OrderUpdateModel;
import com.suryansh.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/api/order")
@CrossOrigin("*")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping("/placeOrder/{userName}")
    @Async
    public CompletableFuture<String> placeOrder(@PathVariable String userName,
                                     @RequestHeader(name = "Authorization") String token) {
        return orderService.placeOrder(userName,token);
    }

    @GetMapping("/getOrder-byUser/{userName}")
    public List<OrderDto> getOrderByUser(@PathVariable String userName,
                                         @RequestHeader(name = "Authorization") String token) {
        return orderService.getAllOrderByUser(userName,token);
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

    @PutMapping("/updateOrder")
    public void updateOrder(@RequestBody OrderUpdateModel orderUpdateModel) {
        orderService.updateOrder(orderUpdateModel);
    }
    @GetMapping("/dummyMail")
    public String sendDummyMail(){
        orderService.sendDummyMail();
        return "Mail Send";
    }
}
