package com.suryansh.userservice.controller;

import com.suryansh.userservice.dto.AddressDto;
import com.suryansh.userservice.dto.LikedProductPaging;
import com.suryansh.userservice.dto.UserDto;
import com.suryansh.userservice.model.AddressModel;
import com.suryansh.userservice.model.LikeModel;
import com.suryansh.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;

    @GetMapping("/test/userService")
    public String testUserService(){
        return "userService is up you may continue";
    }
    @PostMapping("/addUser/{userName}")
    public ResponseEntity<String> saveUser(@PathVariable String userName) {
        try {
            userService.save(userName);
            return new ResponseEntity<>("User Saved Successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to save User", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username) {
        try {
            return new ResponseEntity<>(userService.findUserByName(username)
                    , HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/likeProduct")
    public void likeProduct(@RequestBody LikeModel likeModel) {
        userService.likeProduct(likeModel);
    }

    @PostMapping("/unLikeProduct")
    public void unLikeProduct(@RequestBody LikeModel likeModel) {
        userService.unLikeProduct(likeModel);
    }

    @GetMapping("/isProductLikedByUser/{userName}/{productId}")
    public Boolean checkProductIsLiked(@PathVariable String userName, @PathVariable Long productId) {
        try {
            userService.checkProductIsLikedOrNot(userName, productId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @PostMapping("/addUserAddress")
    public void addAddress(@RequestBody @Valid AddressModel addressModel) {
        userService.addUserAddress(addressModel);
    }

    @PutMapping("/updateUserAddress")
    public ResponseEntity<Void> updateAddress(@RequestBody AddressModel addressModel) {
        try {
            userService.updateUserAddress(addressModel);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getUserAddress/{userName}")
    public AddressDto getAllAddress(@PathVariable String userName) {
        return userService.getUserAddress(userName);
    }
    @GetMapping("/likedProducts-byUser/{userName}")
    @Async
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
