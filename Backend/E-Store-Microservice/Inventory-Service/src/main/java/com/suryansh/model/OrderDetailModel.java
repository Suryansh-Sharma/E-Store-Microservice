package com.suryansh.model;

import java.util.List;

public record OrderDetailModel(long userId, List<product> products){
    public record product(Long productId,Float price,int quantity){}

}