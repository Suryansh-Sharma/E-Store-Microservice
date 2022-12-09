package com.suryansh.userservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeModel {
    private String userName;
    private Long productId;
    private String productName;
}
