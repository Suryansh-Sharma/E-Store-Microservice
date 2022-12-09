package com.suryansh.userservice.service;

import com.suryansh.userservice.dto.CartDto;
import com.suryansh.userservice.dto.CartItems;
import com.suryansh.userservice.dto.InventoryResponse;
import com.suryansh.userservice.dto.ProductDto;
import com.suryansh.userservice.entity.User;
import com.suryansh.userservice.entity.UserCart;
import com.suryansh.userservice.exception.UserServiceException;
import com.suryansh.userservice.model.CartModel;
import com.suryansh.userservice.repository.UserCartRepository;
import com.suryansh.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserCartRepository userCartRepository;
    private final WebClient.Builder webClientBuilder;
    private final UserRepository userRepository;

    @Override
    @Transactional
    @Async
    public void addProductToCart(CartModel cartModel) {
        // Calling Product Microservice for Getting Product.
        ProductDto productResponse = webClientBuilder.build().get()
                .uri("http://geekyprogrammer:8080/api/products/by-id/" + cartModel.getProductId())
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
        User user = userRepository.findByUserName(cartModel.getUserName())
                .orElseThrow(() -> new UserServiceException("Unable to Find User for Add to Cart " +
                        cartModel.getUserName()));
        assert productResponse != null;
        UserCart cart = UserCart.builder()
                .userId(user.getId())
                .productId(productResponse.getId())
                .productName(productResponse.getProductName())
                .price(productResponse.getPrice())
                .totalPrice(productResponse.getPrice() * cartModel.getNoOfProduct())
                .noOfProduct(cartModel.getNoOfProduct())
                .build();
        user.setCartTotalPrice(user.getCartTotalPrice() + cart.getTotalPrice());
        user.setCartTotalProducts(user.getCartTotalProducts() + cart.getNoOfProduct());
        try {
            userRepository.save(user);
            userCartRepository.save(cart);
        } catch (Exception e) {
            throw new UserServiceException("Unable to Save Product to Cart " + productResponse.getProductName());
        }
    }

    @Override
    public CartDto findAllByUserName(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserServiceException("Unable to Find User for Add to Cart " +
                        userName));
        if (user.getCartTotalProducts() == 0) return null;
        List<CartItems> res = user.getCartProducts().stream()
                .map(this::CartEntityToDto)
                .toList();

        return CartDto.builder()
                .cartProduct(res)
                .totalPrice(user.getCartTotalPrice())
                .totalProducts(user.getCartTotalProducts())
                .build();
    }

    @Override
    @Transactional
    @Async
    public void updateCartForUser(List<CartModel> cartModels, String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserServiceException("User is not present : updateCart " + userName));
        cartModels.forEach((model -> {
            if (model.getNoOfProduct() == 0) RemoveProductFromCart(userName, model);
            else {
                UserCart cart = userCartRepository.findByIdAndUserId(model.getId(), user.getId())
                        .orElseThrow(() -> new UserServiceException("Unable to find cart product for " + model));
                ProductDto productResponse = webClientBuilder.build().get()
                        .uri("http://geekyprogrammer:8080/api/products/by-id/" + model.getProductId())
                        .retrieve()
                        .bodyToMono(ProductDto.class)
                        .block();
                user.setCartTotalPrice(user.getCartTotalPrice() - cart.getTotalPrice());
                assert productResponse != null;
                user.setCartTotalPrice(user.getCartTotalPrice() + productResponse.getPrice() *
                        model.getNoOfProduct());
                user.setCartTotalProducts(user.getCartTotalProducts() - cart.getNoOfProduct());
                user.setCartTotalProducts(user.getCartTotalProducts() + model.getNoOfProduct());
                cart.setPrice(productResponse.getPrice());
                cart.setTotalPrice(productResponse.getPrice() * model.getNoOfProduct());
                cart.setNoOfProduct(model.getNoOfProduct());
                try {
                    userCartRepository.save(cart);
                    userRepository.save(user);
                } catch (Exception e) {
                    throw new UserServiceException("Unable to save Updated Cart : " + model);
                }
            }
        }));
    }

    @Override
    @Transactional
    @Async
    public void clearCartForUser(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserServiceException("User is not present : updateCart " + userName));
        user.setCartTotalPrice((float) 0);
        user.setCartTotalProducts(0);
        List<UserCart> carts = user.getCartProducts();
        for (UserCart val : carts) {
            userCartRepository.deleteProductFromCart(val.getId());
        }
    }
    @Transactional
    public void RemoveProductFromCart(String userName, CartModel model) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserServiceException("User is not present : updateCart " + userName));
        UserCart cart = userCartRepository.findByIdAndUserId(model.getId(), user.getId())
                .orElseThrow(() -> new UserServiceException("Unable to find cart product for " + model));
        user.setCartTotalProducts(user.getCartTotalProducts() - cart.getNoOfProduct());
        if (user.getCartTotalProducts() <= 0) user.setCartTotalProducts(0);
        user.setCartTotalPrice(user.getCartTotalPrice() - cart.getTotalPrice());
        try {
            userCartRepository.deleteProductFromCart(cart.getId());
            userRepository.save(user);
            System.out.println("Removed Product from Cart !!");
        } catch (Exception e) {
            throw new UserServiceException("Unable to Delete Product From Cart " + model);
        }
    }

    private CartItems CartEntityToDto(UserCart userCart) {
        // Calling Inventory Repository.
        InventoryResponse productStock = webClientBuilder.build().get()
                .uri("http://geekyprogrammer:8080/api/inventory/get-product-byId/" + userCart.getProductId())
                .retrieve()
                .bodyToMono(InventoryResponse.class)
                .block();
        assert productStock != null;
        Boolean isInStock = productStock.getNoOfStock() >= userCart.getNoOfProduct();
        return CartItems.builder()
                .id(userCart.getId())
                .isInStock(isInStock)
                .productId(userCart.getProductId())
                .productName(userCart.getProductName())
                .price(userCart.getPrice())
                .totalPrice(userCart.getPrice() * userCart.getNoOfProduct())
                .noOfProduct(userCart.getNoOfProduct())
                .build();
    }
}