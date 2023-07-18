package com.suryansh.userservice.controller;

import com.suryansh.userservice.dto.CartCheckDto;
import com.suryansh.userservice.dto.CartDto;
import com.suryansh.userservice.model.CartModel;
import com.suryansh.userservice.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/cart/")
@CrossOrigin("*")
@SecurityRequirement(name = "bearerAuth")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("add-product/{username}/{productId}")
    @Async
    public CompletableFuture<String> AddProductToCart(@PathVariable String username,@PathVariable Long productId) {
        return cartService.addProductToCart(username,productId);
    }

    @GetMapping("of-user/{username}")
    @Async
    public CompletableFuture<CartDto> getAllCartProductsByUser(@PathVariable String username) {
        return cartService.findCartByUserName(username);
    }

    @PostMapping("update-whole-cart/{username}")
    @Async
    public void updateCart(@RequestBody List<CartModel> cartModels,
                                                @PathVariable String username) {
     cartService.updateProductCartForUser(cartModels, username);
    }
    @PostMapping("update-single-product")
    @Async
    public CompletableFuture<String> updateSingleCartProduct(@RequestBody @Valid CartModel cartModel){
        return cartService.updateSingleProductInCart(cartModel);
    }
    @PostMapping("delete-product/{username}/{productId}")
    public String removeProductFromCart(@PathVariable String username,@PathVariable Long productId){
        return cartService.removeProductFromCart(username,productId);
    }
    @GetMapping("is-product-present/{username}/{productId}")
    public boolean isProductInCart(@PathVariable String username, @PathVariable Long productId){
        return cartService.isProductPresentInCart(username,productId);
    }
    @GetMapping("clear/for-user/{username}")
    public void clearCartForUser(@PathVariable String username) {
        cartService.clearCartForUser(username);
    }
}
