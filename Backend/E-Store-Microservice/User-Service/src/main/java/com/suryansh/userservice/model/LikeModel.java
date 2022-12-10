package com.suryansh.userservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LikeModel {
    private String userName;
    private Long productId;
    private String productName;
}
