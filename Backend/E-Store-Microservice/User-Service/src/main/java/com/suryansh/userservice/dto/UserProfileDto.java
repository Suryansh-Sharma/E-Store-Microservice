package com.suryansh.userservice.dto;


public record UserProfileDto(Long id,String username,String nickname,int cartTotalProducts,Float cartTotalPrice,int totalLikedProduct,
                             UserAddress address,UserContact contact) {
    public record UserAddress(String line1,String city,String pinCode,String otherDetails){}
    public record UserContact(String email,String contact,String country,String countryCode){}
}
