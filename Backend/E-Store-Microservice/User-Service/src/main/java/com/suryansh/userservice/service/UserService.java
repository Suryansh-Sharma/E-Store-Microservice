package com.suryansh.userservice.service;

import com.suryansh.userservice.dto.AddressDto;
import com.suryansh.userservice.dto.LikedProductPaging;
import com.suryansh.userservice.dto.UserDto;
import com.suryansh.userservice.model.AddressModel;
import com.suryansh.userservice.model.LikeModel;
import org.springframework.data.domain.Pageable;

import java.util.concurrent.CompletableFuture;

public interface UserService {
    void save(String userName);

    UserDto findUserByName(String userName);

    void likeProduct(LikeModel likeModel);

    void unLikeProduct(LikeModel likeModel);

    void checkProductIsLikedOrNot(String userName, Long productId);

    void addUserAddress(AddressModel addressModel);

    void updateUserAddress(AddressModel addressModel);

    AddressDto getUserAddress(String userName);

    CompletableFuture<LikedProductPaging> getAllLikedProductsByUser(String userName, Pageable pageNo);

    void isUserPresent(String userName);
}
