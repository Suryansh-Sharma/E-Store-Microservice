package com.suryansh.userservice.service;

import com.suryansh.userservice.dto.AddressDto;
import com.suryansh.userservice.dto.LikedProductDto;
import com.suryansh.userservice.dto.UserDto;
import com.suryansh.userservice.model.AddressModel;
import com.suryansh.userservice.model.LikeModel;

import java.util.List;

public interface UserService {
    void save(String userName);

    UserDto findUserByName(String userName);

    void likeProduct(LikeModel likeModel);

    void unLikeProduct(LikeModel likeModel);

    void checkProductIsLikedOrNot(String userName, Long productId);

    void addUserAddress(AddressModel addressModel);

    void updateUserAddress(AddressModel addressModel);

    List<AddressDto> getUserAddress(String userName);

    AddressDto getUserAddressById(Long id);

    List<LikedProductDto> getAllLikedProductsByUser(String userName);

    void isUserPresent(String userName);
}
