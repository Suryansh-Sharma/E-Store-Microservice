package com.suryansh.userservice.service;

import com.suryansh.userservice.dto.CartDto;
import com.suryansh.userservice.model.CartModel;

import java.util.List;

public interface CartService {
    void addProductToCart(CartModel cartModel);

    CartDto findAllByUserName(String userName);

    void updateCartForUser(List<CartModel> cartModels, String userName);

    void clearCartForUser(String userName);
}
