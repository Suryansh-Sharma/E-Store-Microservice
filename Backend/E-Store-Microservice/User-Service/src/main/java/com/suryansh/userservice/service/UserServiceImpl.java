package com.suryansh.userservice.service;

import com.suryansh.userservice.dto.AddressDto;
import com.suryansh.userservice.dto.LikedProductPaging;
import com.suryansh.userservice.dto.ProductDto;
import com.suryansh.userservice.dto.UserDto;
import com.suryansh.userservice.entity.LikedProduct;
import com.suryansh.userservice.entity.User;
import com.suryansh.userservice.entity.UserAddress;
import com.suryansh.userservice.exception.MicroserviceException;
import com.suryansh.userservice.exception.UserServiceException;
import com.suryansh.userservice.model.AddressModel;
import com.suryansh.userservice.model.LikeModel;
import com.suryansh.userservice.repository.LikedProductRepository;
import com.suryansh.userservice.repository.UserAddressRepository;
import com.suryansh.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final LikedProductRepository likedProductRepository;
    private final UserAddressRepository userAddressRepository;
    private final WebClient.Builder webClientBuilder;

    @Override
    @Transactional
    public void save(String userName) {
        Optional<User> user = userRepository.findByUserName(userName);
        if (user.isEmpty()) {
            User newUser = User.builder()
                    .userName(userName)
                    .cartTotalPrice((float) 0)
                    .cartTotalProducts(0)
                    .totalLikedProduct(0)
                    .build();
            userRepository.save(newUser);
            log.info("User Added to database");
        }

    }

    @Override
    public UserDto findUserByName(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserServiceException("Unable to find user for findByUsername " + userName));
        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .cartTotalProducts(user.getCartTotalProducts())
                .cartTotalPrice(user.getCartTotalPrice())
                .totalLikedProduct(user.getTotalLikedProduct())
                .build();
    }

    @Override
    @Transactional
    public void likeProduct(LikeModel likeModel) {
        User user = userRepository.findByUserName(likeModel.getUserName())
                .orElseThrow(() -> new UserServiceException("Unable to Find User for Like " + likeModel.getUserName()));
        likedProductRepository
                .findByUserIdAndProductId(user.getId(), likeModel.getProductId())
                .ifPresent(s -> {
                    throw new UserServiceException("Already Liked this Product");
                });
        LikedProduct likedProduct = LikedProduct.builder()
                .userId(user.getId())
                .productId(likeModel.getProductId())
                .productName(likeModel.getProductName())
                .build();
        user.setTotalLikedProduct(user.getTotalLikedProduct() + 1);
        try {
            likedProductRepository.save(likedProduct);
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Product {} not able to Liked for user {}",likeModel.getProductName(),user.getUserName());
            throw new UserServiceException("Unable Add Product in Liked Table");
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
            log.error("Unable to UnLike product {} for user {} ",likeModel.getProductName(),user.getUserName());
            throw new UserServiceException("Unable to Delete Product From Liked Product");
        }
    }

    @Override
    public void checkProductIsLikedOrNot(String userName, Long productId) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserServiceException("Unable to Find User for Like " + userName));
        try {
            LikedProduct likedProduct = likedProductRepository
                    .findByUserIdAndProductId(user.getId(), productId)
                    .orElseThrow(() -> new UserServiceException("Unable to find Liked product in UnLike Product "));
            if (likedProduct == null) throw new UserServiceException("Product is not present");
        } catch (Exception e) {
            throw new UserServiceException("Product is not Liked by user");
        }
    }

    @Override
    @Transactional
    public void addUserAddress(AddressModel addressModel) {
        User user = userRepository.findByUserName(addressModel.getUserName())
                .orElseThrow(() -> new UserServiceException("Unable to Find User for Address " +
                        addressModel.getUserName()));
        UserAddress userAddress = UserAddress.builder()
                .id(user.getId())
                .line1(addressModel.getLine1())
                .city(addressModel.getCity())
                .pinCode(addressModel.getPinCode())
                .otherDetails(addressModel.getOtherDetails())
                .build();
        try {
            userAddressRepository.save(userAddress);
        } catch (Exception e) {
            throw new UserServiceException("Unable to save Address for User " + user.getUserName());
        }
    }

    @Override
    @Transactional
    public void updateUserAddress(AddressModel addressModel) {
        UserAddress address = userAddressRepository.findById(addressModel.getId())
                .orElseThrow(() -> new UserServiceException("Unable to find Address for User"));
        address.setLine1(addressModel.getLine1());
        address.setCity(addressModel.getCity());
        address.setPinCode(addressModel.getPinCode());
        address.setOtherDetails(addressModel.getOtherDetails());
        try {
            userAddressRepository.save(address);
        } catch (Exception e) {
            throw new UserServiceException("Unable to Update User Address");
        }
    }

    @Override
    public AddressDto getUserAddress(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserServiceException("Unable to Find User for Address " +
                        userName));
        return AddressEntityToDto(user.getUserAddresses());
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
                    .uri("http://PRODUCT-SERVICE/api/products/by-id/"+likedProduct.getProductId())
                    .retrieve()
                    .onStatus(HttpStatus::isError,
                            clientResponse -> Mono.error(
                                    new MicroserviceException("Unable to communicate product service for getAllLikedProduct")
                            ))
                    .bodyToMono(ProductDto.class)
                    .block();
            products.add(product);
        }
        return CompletableFuture.completedFuture(LikedProductPaging.builder()
                .products(products)
                .currentPage(pageable.getPageNumber() + 1)
                .totalPages(page.getTotalPages())
                .build());
    }

    @Override
    @Transactional
    public void isUserPresent(String userName) {
        Optional<User>user=userRepository.findByUserName(userName);
        if (user.isPresent()){
            log.info("User {} is Present in Database: ",userName);
        }else{
            User newUser = User.builder()
                    .userName(userName)
                    .cartTotalPrice((float) 0)
                    .cartTotalProducts(0)
                    .totalLikedProduct(0)
                    .build();
            userRepository.save(newUser);
            log.info("User {} is Added To Database : ",userName);
        }

    }
    private AddressDto AddressEntityToDto(UserAddress userAddress) {
        return AddressDto.builder()
                .id(userAddress.getId())
                .line1(userAddress.getLine1())
                .city(userAddress.getCity())
                .pinCode(userAddress.getPinCode())
                .otherDetails(userAddress.getOtherDetails())
                .build();
    }
}
