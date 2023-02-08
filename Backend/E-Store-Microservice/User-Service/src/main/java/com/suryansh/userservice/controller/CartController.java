package com.suryansh.userservice.controller;

import com.suryansh.userservice.dto.CartCheckDto;
import com.suryansh.userservice.dto.CartDto;
import com.suryansh.userservice.model.CartModel;
import com.suryansh.userservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/cart/")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CartController {
    private final CartService cartService;

    @PostMapping("/addProductToCart")
    @Async
    public CompletableFuture<String> AddProductToCart(@RequestBody CartModel cartModel) {
        return cartService.addProductToCart(cartModel);
    }

    @GetMapping("/getCartByUser/{userName}")
    @ResponseStatus(HttpStatus.OK)
    @Async
    public CompletableFuture<CartDto> getAllCartProductsByUser(@PathVariable String userName,
                                                               @RequestHeader(name = "Authorization") String token) {
        return CompletableFuture.completedFuture(cartService.findAllByUserName(userName,token));
    }

    @PostMapping("/updateCartOfUser/{userName}")
    @Async
    public void updateCart(@RequestBody List<CartModel> cartModels,
                                                @PathVariable String userName,
                                                @RequestHeader(name = "Authorization") String token) {
     cartService.updateProductCartForUser(cartModels, userName, token);
    }
    @PostMapping("/deleteProductFromCart/{userName}/{cartId}")
    public String removeProductFromCart(@PathVariable String userName,@PathVariable Long cartId){
        return cartService.removeProductFromCart(userName,cartId);
    }
    @GetMapping("/isProductInCart/{username}/{productId}")
    public CartCheckDto isProductInCart(@PathVariable String username, @PathVariable Long productId){
        return cartService.isProductPresentInCart(username,productId);
    }
    @GetMapping("/clearCartForUser/{userName}")
    @Async
    public CompletableFuture<String> clearCartForUser(@PathVariable String userName) {
        cartService.clearCartForUser(userName);
        return CompletableFuture.completedFuture("Cart is updated for user : - " + userName);
    }
}
