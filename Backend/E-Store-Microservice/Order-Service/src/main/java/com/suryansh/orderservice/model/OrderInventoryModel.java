package com.suryansh.orderservice.model;

import java.util.List;


public record OrderInventoryModel(long userId, List<product> products){
    public record product(Long productId,Float price,int quantity){}

}