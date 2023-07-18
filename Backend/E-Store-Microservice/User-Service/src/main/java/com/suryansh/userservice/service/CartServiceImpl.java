package com.suryansh.userservice.service;

import com.suryansh.userservice.dto.CartDto;
import com.suryansh.userservice.dto.CartItems;
import com.suryansh.userservice.dto.ProductDto;
import com.suryansh.userservice.entity.User;
import com.suryansh.userservice.entity.UserCart;
import com.suryansh.userservice.exception.MicroserviceException;
import com.suryansh.userservice.exception.UserServiceException;
import com.suryansh.userservice.model.CartModel;
import com.suryansh.userservice.repository.UserCartRepository;
import com.suryansh.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserCartRepository userCartRepository;
    private final WebClient.Builder webClientBuilder;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Override
    @Async
    @Transactional
    public CompletableFuture<String> addProductToCart(String username,Long productId) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UserServiceException("Unable to Find User for Add to Cart " +
                        username));
        Optional<UserCart>cartProduct = userCartRepository.getUserCartByUserAndProductId(user, productId);
        if (cartProduct.isPresent()){
            return CompletableFuture.failedFuture(
                new UserServiceException("Sorry but you already added this product is cart !!")
            );
        }
        // Calling Inventory Service to get stock update.
        Boolean inventoryResponse = webClientBuilder.build().get()
                .uri("https://INVENTORY-SERVICE/api/inventory/check/availability/"+
                        productId+"/quantity/"+1
                )
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(
                                new MicroserviceException("Unable to save product because Inventory service is down ," +
                                        " Please try after some time"))
                )
                .onStatus(HttpStatusCode::is4xxClientError, clientRes ->
                        Mono.error(new UserServiceException("Sorry !! , Product is out of stock ")
                        )
                )
                .bodyToMono(Boolean.class)
                .block();
        if (Boolean.FALSE.equals(inventoryResponse))throw new UserServiceException("Sorry !! , Product is out of stock ");

        // Calling Product Microservice for Getting Product.
        ProductDto productResponse = webClientBuilder.build().get()
                .uri("http://PRODUCT-SERVICE/api/products/by-id/" + productId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        Mono.error(new MicroserviceException(
                                        "Unable to Communicate Product Service for AddProductToCart"
                                )
                        )
                )
                .bodyToMono(ProductDto.class)
                .block();
        if (productResponse == null) throw new UserServiceException("Product not found for AddToCart");
        float price = Float.parseFloat(productResponse.price().value());
        UserCart cart = UserCart.builder()
                .productId(productResponse.id())
                .productName(productResponse.title())
                .productImage(productResponse.imageUrl())
                .price(price)
                .totalPrice(price * 1)
                .noOfProduct(1)
                .user(user)
                .build();
        user.setCartTotalPrice(user.getCartTotalPrice() + cart.getTotalPrice());
        user.setCartTotalProducts(user.getCartTotalProducts() + 1);
        user.getCartProducts().add(cart);
        try {
            userRepository.save(user);
            logger.info("Product {} is added to cart by user {} ",productResponse.id(),user.getUserName());
            return CompletableFuture.completedFuture("Product Added to cart Successfully");
        } catch (MicroserviceException e){
            logger.error("Service is down "+e);
            return CompletableFuture.failedFuture(new UserServiceException("Some Service is down "+e.getMessage()));
        } catch (Exception e) {
            logger.error("Unable to add product to cart "+e);
            throw new UserServiceException("Unable to Save Product to Cart " +e);
        }
    }
    @Override
    @Async
    @Transactional
    public CompletableFuture<CartDto> findCartByUserName(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserServiceException("Unable to Find User for Add to Cart " +
                        userName));
        // If cart is empty.
        if (user.getCartTotalProducts() == 0) {
            return CompletableFuture.completedFuture(
                    CartDto.builder()
                            .cartProduct(null)
                            .totalPrice(user.getCartTotalPrice())
                            .totalProducts(user.getCartTotalProducts())
                            .build()
            );
        }
        List<CartItems>cartItems=user.getCartProducts().stream()
                .map(this::CartEntityToDto)
                .toList();
        return CompletableFuture.completedFuture(
                CartDto.builder()
                        .cartProduct(cartItems)
                        .totalPrice(user.getCartTotalPrice())
                        .totalProducts(user.getCartTotalProducts())
                        .build()
        );
    }
    @Override
    @Transactional
    @Async
    public void updateProductCartForUser(List<CartModel> cartModels, String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserServiceException("User is not present : updateCart " + userName));
        cartModels.forEach(model -> {
            if (model.getNoOfProduct() == 0) removeProductFromCart(userName, model.getProductId());
            else {
                UserCart cart = userCartRepository.getUserCartByUserAndProductId(user, model.getProductId())
                        .orElseThrow(()->new UserServiceException("Unable to find product in cart "+model.getProductId()));
                ProductDto productResponse = webClientBuilder.build().get()
                        .uri("http://PRODUCT-SERVICE/api/products/by-id/" + model.getProductId())
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new MicroserviceException(
                                "Unable to Communicate Product Service for AddProductToCart"
                        )))
                        .bodyToMono(ProductDto.class)
                        .block();
                if (productResponse == null)
                    throw new UserServiceException("Unable to find product of id : - " + model.getProductId());
                float newPrice = Float.parseFloat(productResponse.price().value());
                user.setCartTotalPrice(user.getCartTotalPrice() - cart.getTotalPrice());
                user.setCartTotalPrice(user.getCartTotalPrice() + newPrice *
                        model.getNoOfProduct());
                cart.setPrice(newPrice);
                cart.setTotalPrice(newPrice * model.getNoOfProduct());
                cart.setProductImage(productResponse.imageUrl());
                cart.setNoOfProduct(model.getNoOfProduct());
                user.getCartProducts().add(cart);
                try {
                    userRepository.save(user);
                    log.info("Cart Updated Successfully");
                } catch (Exception e) {
                    CompletableFuture.failedFuture(new
                            UserServiceException("User Address is not Available. :placeOrder"));
                }
            }
        });
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<String> updateSingleProductInCart(CartModel cartModel) {
        var user = userRepository.findByUserName(cartModel.getUserName())
                .orElseThrow(() -> new UserServiceException("User is not present : updateCart " + cartModel.getUserName()));
        UserCart cart = userCartRepository.getUserCartByUserAndProductId(user, cartModel.getProductId())
                .orElseThrow(()->new UserServiceException("Unable to find product in cart "+cartModel.getProductId()));
        if (cartModel.getNoOfProduct() == 0) {
            removeProductFromCart(user.getUserName(), cartModel.getProductId());
        }else{
            // Calling Inventory Service to check stock of product.
            Boolean inventoryResponse = webClientBuilder.build().get()
                    .uri("http://Inventory-SERVICE/api/inventory/check/availability/"+
                            cartModel.getProductId()+"/quantity/"+cartModel.getNoOfProduct()
                    )
                    .retrieve()
                    .onStatus(HttpStatusCode::is5xxServerError,e->
                            Mono.error(
                                    new MicroserviceException("Sorry inventory is not responding currently !!")
                            )
                    )
                    .bodyToMono(Boolean.class)
                    .block();

            if (Boolean.FALSE.equals(inventoryResponse))return CompletableFuture.failedFuture(
                new UserServiceException("Sorry !! , Product is out of stock ")
            );

            ProductDto productResponse = webClientBuilder.build().get()
                    .uri("http://PRODUCT-SERVICE/api/products/by-id/" + cartModel.getProductId())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new MicroserviceException(
                            "Unable to Communicate Product Service for AddProductToCart"
                    )))
                    .bodyToMono(ProductDto.class)
                    .block();

            if (productResponse==null) return CompletableFuture.failedFuture(
                    new UserServiceException("Unable to find product of id "+ cartModel.getProductId())
            );
            float price = Float.parseFloat(productResponse.price().value()) * cartModel.getNoOfProduct();
            user.setCartTotalProducts(user.getCartTotalProducts()-cart.getNoOfProduct());
            user.setCartTotalPrice(user.getCartTotalPrice()-cart.getTotalPrice());
            cart.setNoOfProduct(cartModel.getNoOfProduct());
            cart.setPrice(price);
            user.setCartTotalProducts(user.getCartTotalProducts()+cart.getNoOfProduct());
            user.setCartTotalPrice(user.getCartTotalPrice()+price);
        }try {
            userCartRepository.save(cart);
            userRepository.save(user);
            return CompletableFuture.completedFuture("User Cart is updated successfully");
        }catch (Exception e){
            logger.error("Unable to update product inside cart "+e);
            return CompletableFuture.failedFuture(
                new UserServiceException("Exception :  "+e.getMessage())
            );
        }
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
    public String removeProductFromCart(String userName, Long productId) {
        var user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserServiceException("User is not present : updateCart " + userName));
        Optional<UserCart> productToRemove = user.getCartProducts().stream()
                .filter(p->p.getProductId().equals(productId))
                .findFirst();
        if (productToRemove.isEmpty())throw new UserServiceException("Unable to find product of id : "+productId);
        user.setCartTotalProducts(user.getCartTotalProducts() - productToRemove.get().getNoOfProduct());
        if (user.getCartTotalProducts() <= 0) user.setCartTotalProducts(0);
        user.setCartTotalPrice(user.getCartTotalPrice() - productToRemove.get().getTotalPrice());
        if (user.getCartTotalPrice()<=0)user.setCartTotalPrice(0F);
        try {
            user.getCartProducts().remove(productToRemove.get());
            userRepository.save(user);
            logger.info("Removed Product {}  from Cart of user {} !!",productToRemove.get().getProductId(),user.getUserName());
            return "Product removed successfully from cart.";
        } catch (Exception e) {
            logger.error("Unable to remove product from cart !! "+e);
            throw new UserServiceException("Unable to Delete Product From Cart ");
        }
    }

    @Override
    public boolean isProductPresentInCart(String username, Long productId) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UserServiceException("Unable to find user of name :- " + username));
        UserCart cart = userCartRepository.findFirstByUserIdAndProductId(user.getId(), productId);
        return cart != null;
    }
    private CartItems CartEntityToDto(UserCart userCart) {
        // Calling Inventory Repository.
        Boolean inventoryResponse = webClientBuilder.build().get()
                .uri("http://INVENTORY-SERVICE/api/inventory/check/availability/" +
                        userCart.getProductId() + "/quantity/" + userCart.getNoOfProduct()
                )
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new MicroserviceException("Unable to save product because the Inventory service is down. " +
                                "Please try again later."))
                )
                .onStatus(HttpStatusCode::is4xxClientError, clientRes ->
                        Mono.error(new UserServiceException("Sorry! The product is out of stock."))
                )
                .bodyToMono(Boolean.class)
                .block();
        if (Boolean.FALSE.equals(inventoryResponse))throw new UserServiceException("Sorry !! , Product is out of stock ");
        return CartItems.builder()
                .id(userCart.getId())
                .isInStock(inventoryResponse)
                .productId(userCart.getProductId())
                .productName(userCart.getProductName())
                .productImage(userCart.getProductImage())
                .imageUrl(userCart.getProductImage())
                .price(userCart.getPrice())
                .totalPrice(userCart.getPrice() * userCart.getNoOfProduct())
                .noOfProduct(userCart.getNoOfProduct())
                .build();
    }
}
