package com.suryansh.userservice.service;

import com.suryansh.userservice.dto.LikedProductPaging;
import com.suryansh.userservice.dto.ProductDto;
import com.suryansh.userservice.dto.UserProfileDto;
import com.suryansh.userservice.entity.LikedProduct;
import com.suryansh.userservice.entity.User;
import com.suryansh.userservice.entity.UserAddress;
import com.suryansh.userservice.entity.UserContact;
import com.suryansh.userservice.exception.MicroserviceException;
import com.suryansh.userservice.exception.UserServiceException;
import com.suryansh.userservice.model.LikeModel;
import com.suryansh.userservice.model.UserProfileModel;
import com.suryansh.userservice.repository.LikedProductRepository;
import com.suryansh.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final LikedProductRepository likedProductRepository;
    private final WebClient.Builder webClientBuilder;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    @Transactional
    public String save(String userName) {
        Optional<User> user = userRepository.findByUserName(userName);
        if (user.isPresent()) {
            throw new UserServiceException("Username is already present !!");
        }
        int atIndex = userName.indexOf('@');
        User newUser = User.builder()
                .userName(userName)
                .nickname(userName.substring(0, atIndex))
                .cartTotalPrice((float) 0)
                .cartTotalProducts(0)
                .totalLikedProduct(0)
                .userAddresses(null)
                .userContact(null)
                .build();
        try {
            userRepository.save(newUser);
            logger.info("User {} Added to database ", userName);
            return "User " + userName + " is successfully added ";
        } catch (Exception e) {
            logger.error("Unable to save user " + e);
            throw new UserServiceException("Unable to save user ");
        }
    }

    @Override
    public UserProfileDto findUserByName(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserServiceException("Unable to find user for findByUsername " + userName));
        UserProfileDto.UserAddress address = new UserProfileDto.UserAddress(
                user.getUserAddresses().getLine1(),
                user.getUserAddresses().getCity(),
                user.getUserAddresses().getPinCode(),
                user.getUserAddresses().getOtherDetails()
        );

        UserProfileDto.UserContact contact = new UserProfileDto.UserContact(
                user.getUserContact().getEmail(),
                user.getUserContact().getContact(),
                user.getUserContact().getCountry(),
                user.getUserContact().getCountryCode()
        );

        return new UserProfileDto(
                user.getId(),
                user.getUserName(),
                user.getNickname(),
                user.getCartTotalProducts(),
                user.getCartTotalPrice(),
                user.getTotalLikedProduct(),
                address,
                contact
        );
    }

    @Override
    @Transactional
    public void likeProduct(LikeModel likeModel) {
        User user = userRepository.findByUserName(likeModel.getUserName())
                .orElseThrow(() -> new UserServiceException("Unable to Find User for Like " + likeModel.getUserName()));
        user.getLikedProducts()
                .forEach(p -> {
                    if (p.getProductId().equals(likeModel.getProductId()))
                        throw new UserServiceException("Already liked product");
                });
        ProductDto productDto = webClientBuilder.build().get()
                .uri("http://PRODUCT-SERVICE/api/products/by-id/" + likeModel.getProductId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, r ->
                        Mono.error(
                                new UserServiceException("Unable to find product of id " + likeModel.getProductId())
                        )
                )
                .onStatus(HttpStatusCode::is5xxServerError, r ->
                        Mono.error(
                                new UserServiceException("Sorry Product Service is not responding , please try after some time")
                        )
                )
                .bodyToMono(ProductDto.class)
                .block();
        if (productDto == null) throw new UserServiceException("Product is empty ");
        LikedProduct likedProduct = LikedProduct.builder()
                .user(user)
                .productId(productDto.id())
                .build();
        user.setTotalLikedProduct(user.getTotalLikedProduct() + 1);
        user.getLikedProducts().add(likedProduct);
        try {
            userRepository.save(user);
            logger.info("Product {} is liked by user {} ", productDto.id(), user.getUserName());
        } catch (Exception e) {
            logger.error("Product {} not able to Liked for user {}", likeModel.getProductName(), user.getUserName(), e);
            throw new UserServiceException("Exception : " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void unLikeProduct(LikeModel likeModel) {
        User user = userRepository.findByUserName(likeModel.getUserName())
                .orElseThrow(() -> new UserServiceException("Unable to Find User for Like " + likeModel.getUserName()));
        LikedProduct likedProduct = likedProductRepository
                .findByUserIdAndProductId(user.getId(), likeModel.getProductId())
                .orElseThrow(() -> new UserServiceException("Unable to find Liked product in UnLike Product "));
        if (user.getTotalLikedProduct() > 0) user.setTotalLikedProduct(user.getTotalLikedProduct() - 1);
        try {
            likedProductRepository.delete(likedProduct);
        } catch (Exception e) {
            logger.error("Unable to UnLike product {} for user {} ", likeModel.getProductName(), user.getUserName());
            throw new UserServiceException("Unable to Delete Product From Liked Product");
        }
    }

    @Override
    public boolean checkProductIsLikedOrNot(String userName, Long productId) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserServiceException("Unable to Find User for Like " + userName));
        boolean res = false;
        for (LikedProduct p : user.getLikedProducts()) {
            if (p.getProductId().equals(productId)) {
                res = true;
                break;
            }
        }
        return res;
    }

    @Override
    @Transactional
    public UserProfileModel updateUserProfile(UserProfileModel model) {
        var user = userRepository.findByUserName(model.getUserName())
                .orElseThrow(() -> new UserServiceException("Unable to find user " + model.getUserName()));
        var userAddress = UserAddress.builder()
                .line1(model.getUserAddressModel().getLine1())
                .city(model.getUserAddressModel().getCity())
                .pinCode(model.getUserAddressModel().getPinCode())
                .otherDetails(model.getUserAddressModel().getOtherDetails())
                .build();
        var userContact = UserContact.builder()
                .email(model.getUserContactModel().getEmail())
                .contact(model.getUserContactModel().getContact())
                .country(model.getUserContactModel().getCountry())
                .countryCode(model.getUserContactModel().getCountryCode())
                .user(user)
                .build();
        user.setNickname(model.getNickname());
        user.setUserAddresses(userAddress);
        user.setUserContact(userContact);
        try {
            userRepository.save(user);
            logger.info("User {} profile is updated ", user.getUserName());
            return model;
        } catch (Exception e) {
            logger.error("User {} profile is not updated ", user.getUserName());
            throw new UserServiceException("Unable to update User Profile ");
        }
    }

    @Override
    @Async
    public CompletableFuture<LikedProductPaging> getAllLikedProductsByUser(String userName, Pageable pageable) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new UserServiceException("Unable to find User"));
        Page<LikedProduct> page = likedProductRepository.findByUserId(user.getId(),pageable);
        List<ProductDto> products = new ArrayList<>();
        for (LikedProduct likedProduct : page.getContent()){
            ProductDto product= webClientBuilder.build()
                    .get()
                    .uri("http://PRODUCT-SERVICE/api/products/by-id/" + likedProduct.getProductId())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError,
                            clientResponse -> Mono.error(
                                    new MicroserviceException("Unable to communicate product service for getAllLikedProduct")
                            ))
                    .bodyToMono(ProductDto.class)
                    .block();
            products.add(product);
        }
        return CompletableFuture.completedFuture(
                new LikedProductPaging(products, pageable.getPageNumber() + 1, page.getTotalPages())
        );
    }

    @Override
    @Transactional
    public void isUserPresent(String userName) {
        Optional<User>user=userRepository.findByUserName(userName);
        if (user.isPresent()){
            logger.info("User {} is Present in Database: ", userName);
        }else{
            User newUser = User.builder()
                    .userName(userName)
                    .cartTotalPrice((float) 0)
                    .cartTotalProducts(0)
                    .totalLikedProduct(0)
                    .userAddresses(null)
                    .userContact(null)
                    .build();
            userRepository.save(newUser);
            logger.info("User {} is Added To Database : ", userName);
        }

    }

}
