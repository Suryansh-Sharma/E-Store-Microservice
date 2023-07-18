package com.suryansh.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class FallbackController {

    @GetMapping("/productServiceFallBack")
    public String productServiceFallBackGet(){
        return "Product Service is Down !! , Please try After Sometime";
    }
    @PostMapping("/productServiceFallBack")
    public String productServiceFallBackPost(){
        return "Product Service is Down !! , Please try After Sometime";
    }

    @GetMapping("/inventoryServiceFallBack")
    public String inventoryServiceFallBackGet(){
        return "Inventory Service is Down !! , Please try After Sometime";
    }
    @PostMapping("/inventoryServiceFallBack")
    public String inventoryServiceFallBackPost(){
        return "Inventory Service is Down !! , Please try After Sometime";
    }

    @GetMapping("/orderServiceFallBack")
    public String orderServiceFallBack(){
        return "Order Service is Down !! , Please try After Sometime";
    }
    @GetMapping("/userServiceFallBack")
    public String userServiceFallBack(){
        return "User Service is Down !! , Please try After Sometime";
    }
    @GetMapping("/reviewServiceFallBack")
    public String reviewServiceDown(){return"Review And Question Service is Down !! , Please try After Sometime";}
}
