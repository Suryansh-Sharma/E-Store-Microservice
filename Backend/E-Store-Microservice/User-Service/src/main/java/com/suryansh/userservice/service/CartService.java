package com.suryansh.userservice.service;

import com.suryansh.userservice.dto.CartDto;
import com.suryansh.userservice.model.CartModel;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CartService {
    CompletableFuture<String> addProductToCart(String username,Long productId);

    CompletableFuture<CartDto> findCartByUserName(String userName);
    void clearCartForUser(String userName);

    String removeProductFromCart(String userName, Long productId);

    boolean isProductPresentInCart(String username, Long productId);

    void updateProductCartForUser(List<CartModel> cartModels, String userName);

    CompletableFuture<String> updateSingleProductInCart(CartModel cartModel);
}
