package com.suryansh.userservice.controller;

import com.suryansh.userservice.dto.AddressDto;
import com.suryansh.userservice.dto.LikedProductDto;
import com.suryansh.userservice.dto.UserDto;
import com.suryansh.userservice.model.AddressModel;
import com.suryansh.userservice.model.LikeModel;
import com.suryansh.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {
    private final UserService userService;

    @GetMapping("/test/userService")
    public String testUserService(){
        return "userService is up you may continue";
    }
    @PostMapping("addUser/{userName}")
    public ResponseEntity<String> saveUser(@PathVariable String userName) {
        try {
            userService.save(userName);
            return new ResponseEntity<>("User Saved Successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to save User", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/by-userName/{userName}")
    public ResponseEntity<UserDto> getUser(@PathVariable String userName) {
        try {
            return new ResponseEntity<>(userService.findUserByName(userName)
                    , HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/likeProduct")
    public void likeProduct(@RequestBody LikeModel likeModel) {
        userService.likeProduct(likeModel);
    }

    @DeleteMapping("/unLikeProduct")
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
    public void addAddress(@RequestBody AddressModel addressModel) {
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

    @GetMapping("/getUserAllAddress/{userName}")
    public List<AddressDto> getAllAddress(@PathVariable String userName) {
        return userService.getUserAddress(userName);
    }

    @GetMapping("/getUserAddressById/{id}")
    public AddressDto getUserAddressById(@PathVariable Long id) {
        return userService.getUserAddressById(id);
    }
    @GetMapping("/likedProducts-byUser/{userName}")
    public List<LikedProductDto> getAllLikedProducts(@PathVariable String userName){
        return userService.getAllLikedProductsByUser(userName);
    }
    @GetMapping("isUserPresent/{userName}")
    public void checkUserIsPresent(@PathVariable String userName){
        userService.isUserPresent(userName);
    }

}
