package com.suryansh.userservice.service;

import com.suryansh.userservice.dto.CartCheckDto;
import com.suryansh.userservice.dto.CartDto;
import com.suryansh.userservice.model.CartModel;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CartService {
    CompletableFuture<String> addProductToCart(CartModel cartModel);

    CartDto findAllByUserName(String userName, String token);
    void clearCartForUser(String userName);

    String removeProductFromCart(String userName, Long cartId);

    CartCheckDto isProductPresentInCart(String username, Long productId);

    void updateProductCartForUser(List<CartModel> cartModels, String userName, String token);
}
