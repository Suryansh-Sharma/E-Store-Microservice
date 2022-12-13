package com.suryansh.userservice.service;

import com.suryansh.userservice.dto.CartDto;
import com.suryansh.userservice.model.CartModel;

import java.util.List;

public interface CartService {
    void addProductToCart(CartModel cartModel, String token);

    CartDto findAllByUserName(String userName, String token);

    void updateCartForUser(List<CartModel> cartModels, String userName, String token);

    void clearCartForUser(String userName);
}
