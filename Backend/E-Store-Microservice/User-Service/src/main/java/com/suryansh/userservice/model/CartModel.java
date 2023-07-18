package com.suryansh.userservice.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
public class CartModel {
    @NotBlank(message = "username can't be blank")
    private String userName;
    @Min(value = 0,message = "product id can't be less than 0")
    private Long productId;
    @Min(value = 0,message = "no of product can't be less than 0")
    private int noOfProduct;
}
