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
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserCartRepository userCartRepository;
    private final WebClient.Builder webClientBuilder;
    private final UserRepository userRepository;

    @Override
    @Transactional
    @Async
    public void addProductToCart(CartModel cartModel, String token) {
        // Calling Product Microservice for Getting Product.
        ProductDto productResponse = webClientBuilder.build().get()
                .uri("http://geekyprogrammer:8080/api/products/by-id/" + cartModel.getProductId())
                .header("Authorization",token)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
        log.info("Found Product from Product Service");
        User user = userRepository.findByUserName(cartModel.getUserName())
                .orElseThrow(() -> new UserServiceException("Unable to Find User for Add to Cart " +
                        cartModel.getUserName()));
        log.info("Found User");
        assert productResponse != null;
        UserCart cart = UserCart.builder()
                .userId(user.getId())
                .productId(productResponse.getId())
                .productName(productResponse.getProductName())
                .productImage(productResponse.getProductImage())
                .price(productResponse.getPrice())
                .totalPrice(productResponse.getPrice() * cartModel.getNoOfProduct())
                .noOfProduct(cartModel.getNoOfProduct())
                .build();
        user.setCartTotalPrice(user.getCartTotalPrice() + cart.getTotalPrice());
        user.setCartTotalProducts(user.getCartTotalProducts() + cartModel.getNoOfProduct());
        try {
            userRepository.save(user);
            userCartRepository.save(cart);
            log.info("Added Product Cart");
        } catch (Exception e) {
            throw new UserServiceException("Unable to Save Product to Cart " + productResponse.getProductName());
        }
    }

    @Override
    public CartDto findAllByUserName(String userName, String token) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserServiceException("Unable to Find User for Add to Cart " +
                        userName));
        if (user.getCartTotalProducts() == 0) return null;
        List<CartItems>cartItems=user.getCartProducts().stream()
                .map((userCart)->CartEntityToDto(userCart,token))
                .toList();
        return CartDto.builder()
                .cartProduct(cartItems)
                .totalPrice(user.getCartTotalPrice())
                .totalProducts(user.getCartTotalProducts())
                .build();
    }

    @Override
    @Transactional
    @Async
    public void updateCartForUser(List<CartModel> cartModels, String userName, String token) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserServiceException("User is not present : updateCart " + userName));
        cartModels.forEach((model -> {
            if (model.getNoOfProduct() == 0) removeProductFromCart(userName, model);
            else {
                UserCart cart = userCartRepository.findByIdAndUserId(model.getId(), user.getId())
                        .orElseThrow(() -> new UserServiceException("Unable to find cart product for " + model));
                ProductDto productResponse = webClientBuilder.build().get()
                        .uri("http://geekyprogrammer:8080/api/products/by-id/" + model.getProductId())
                        .header("Authorization",token)
                        .retrieve()
                        .bodyToMono(ProductDto.class)
                        .block();
                user.setCartTotalPrice(user.getCartTotalPrice() - cart.getTotalPrice());
                assert productResponse != null;
                user.setCartTotalPrice(user.getCartTotalPrice() + productResponse.getPrice() *
                        model.getNoOfProduct());
                cart.setPrice(productResponse.getPrice());
                cart.setTotalPrice(productResponse.getPrice() * model.getNoOfProduct());
                cart.setProductImage(productResponse.getProductImage());
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
    @Override
    @Transactional
    public void removeProductFromCart(String userName, CartModel model) {
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
            log.info("Removed Product from Cart !!");
        } catch (Exception e) {
            throw new UserServiceException("Unable to Delete Product From Cart " + model);
        }
    }

    private CartItems CartEntityToDto(UserCart userCart,String token) {
        // Calling Inventory Repository.
        InventoryResponse productStock = webClientBuilder.build().get()
                .uri("http://geekyprogrammer:8080/api/inventory/get-product-byId/" + userCart.getProductId())
                .header("Authorization",token)
                .retrieve()
                .bodyToMono(InventoryResponse.class)
                .block();
        assert productStock != null;
        Boolean isInStock = productStock.getNoOfStock() >= userCart.getNoOfProduct();
        return CartItems.builder()
                .id(userCart.getId())
                .isInStock(isInStock)
                .noOfStock(productStock.getNoOfStock())
                .productId(userCart.getProductId())
                .productName(userCart.getProductName())
                .productImage(userCart.getProductImage())
                .imageUrl("http://geekyprogrammer:8080/api/image/download/"+userCart.getProductImage())
                .price(userCart.getPrice())
                .totalPrice(userCart.getPrice() * userCart.getNoOfProduct())
                .noOfProduct(userCart.getNoOfProduct())
                .build();
    }
}
