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
    public ResponseEntity<Void> likeProduct(@RequestBody LikeModel likeModel) {
        try {
            userService.likeProduct(likeModel);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/unLikeProduct")
    public ResponseEntity<Void> unLikeProduct(@RequestBody LikeModel likeModel) {
        try {
            userService.unLikeProduct(likeModel);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/isProductLikedByUser/{userName}/{productId}")
    public ResponseEntity<Boolean> checkProductIsLiked(@PathVariable String userName, @PathVariable Long productId) {
        try {
            userService.checkProductIsLikedOrNot(userName, productId);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addUserAddress")
    public ResponseEntity<Void> addAddress(@RequestBody AddressModel addressModel) {
        try {
            userService.addUserAddress(addressModel);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
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
    public ResponseEntity<List<AddressDto>> getAllAddress(@PathVariable String userName) {
        try {
            return new ResponseEntity<>(userService.getUserAddress(userName)
                    , HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getUserAddressById/{id}")
    public ResponseEntity<AddressDto> getUserAddressById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(userService.getUserAddressById(id)
                    , HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
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
