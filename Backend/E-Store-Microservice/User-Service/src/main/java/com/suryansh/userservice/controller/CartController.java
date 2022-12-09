package com.suryansh.userservice.controller;

import com.suryansh.userservice.dto.CartDto;
import com.suryansh.userservice.model.CartModel;
import com.suryansh.userservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin
public class CartController {
    private final CartService cartService;

    @PostMapping("/addProductToCart")
    @Async
    public CompletableFuture<ResponseEntity<Void>> AddProductToCart(@RequestBody CartModel cartModel) {
        try {
            cartService.addProductToCart(cartModel);
            return CompletableFuture.completedFuture(new ResponseEntity<>(null, HttpStatus.OK));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
        }
    }

    @GetMapping("/getCartByUser/{userName}")
    @ResponseStatus(HttpStatus.OK)
    @Async
    public CompletableFuture<CartDto> getAllCartProductsByUser(@PathVariable String userName) {
        return CompletableFuture.completedFuture(cartService.findAllByUserName(userName));
    }

    @PostMapping("/updateCartOfUser/{userName}")
    @Async
    public CompletableFuture<String> updateCart(@RequestBody List<CartModel> cartModels,
                                                @PathVariable String userName) {
        cartService.updateCartForUser(cartModels, userName);
        return CompletableFuture.completedFuture("Cart is updated for user : - " + userName);
    }

    @GetMapping("clearCartForUser/{userName}")
    @Async
    public CompletableFuture<String> clearCartForUser(@PathVariable String userName) {
        cartService.clearCartForUser(userName);
        return CompletableFuture.completedFuture("Cart is updated for user : - " + userName);
    }
}