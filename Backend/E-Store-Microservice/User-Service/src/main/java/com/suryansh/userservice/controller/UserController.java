package com.suryansh.userservice.controller;

import com.suryansh.userservice.dto.LikedProductPaging;
import com.suryansh.userservice.dto.UserProfileDto;
import com.suryansh.userservice.model.LikeModel;
import com.suryansh.userservice.model.UserProfileModel;
import com.suryansh.userservice.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/user/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    @GetMapping("test/userService")
    public String testUserService(){
        return "userService is up you may continue";
    }
    @PostMapping("new/{userName}")
    public String saveUser(@PathVariable String userName) {
        return userService.save(userName);
    }
    @GetMapping("by-username/{username}")
    public UserProfileDto getUser(@PathVariable String username) {
        return userService.findUserByName(username);
    }

    @PostMapping("like-product")
    @SecurityRequirement(name = "bearerAuth")
    public void likeProduct(@RequestBody LikeModel likeModel) {
        userService.likeProduct(likeModel);
    }

    @PostMapping("unlike-product")
    @SecurityRequirement(name = "bearerAuth")
    public void unLikeProduct(@RequestBody LikeModel likeModel) {
        userService.unLikeProduct(likeModel);
    }

    @GetMapping("is-product-liked-by-user/{userName}/{productId}")
    public boolean checkProductIsLiked(@PathVariable String userName, @PathVariable Long productId) {
        return userService.checkProductIsLikedOrNot(userName, productId);
    }
    @PostMapping("update-profile")
    @SecurityRequirement(name = "bearerAuth")
    public UserProfileModel updateUserProfile(@RequestBody @Valid UserProfileModel model){
        return userService.updateUserProfile(model);
    }
    @GetMapping("liked-products/{userName}")
    @Async
    @SecurityRequirement(name = "bearerAuth")
    public CompletableFuture<LikedProductPaging> getAllLikedProducts
            (@PathVariable String userName,
             @RequestParam(name = "pageNo",defaultValue = "0",required = false)
                int pageNo){
        Pageable pageable = PageRequest.of(pageNo,6);
        return userService.getAllLikedProductsByUser(userName,pageable);
    }
    @GetMapping("isUserPresent/{userName}")
    public void checkUserIsPresent(@PathVariable String userName){
        userService.isUserPresent(userName);
    }

}
