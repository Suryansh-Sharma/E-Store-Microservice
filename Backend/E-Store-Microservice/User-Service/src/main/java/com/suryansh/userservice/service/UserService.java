package com.suryansh.userservice.service;

import com.suryansh.userservice.dto.LikedProductPaging;
import com.suryansh.userservice.dto.UserProfileDto;
import com.suryansh.userservice.model.LikeModel;
import com.suryansh.userservice.model.UserProfileModel;
import org.springframework.data.domain.Pageable;

import java.util.concurrent.CompletableFuture;

public interface UserService {
    String save(String userName);

    UserProfileDto findUserByName(String userName);

    void likeProduct(LikeModel likeModel);

    void unLikeProduct(LikeModel likeModel);

    boolean checkProductIsLikedOrNot(String userName, Long productId);


    CompletableFuture<LikedProductPaging> getAllLikedProductsByUser(String userName, Pageable pageNo);

    void isUserPresent(String userName);

    UserProfileModel updateUserProfile(UserProfileModel model);
}
