package com.suryansh.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class FallbackController {

    @GetMapping("/productServiceFallBack")
    public String productServiceFallBack(){
        return "Product Service is Down !! , Please try After Sometime";
    }
    @GetMapping("/inventoryServiceFallBack")
    public String inventoryServiceFallBack(){
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
