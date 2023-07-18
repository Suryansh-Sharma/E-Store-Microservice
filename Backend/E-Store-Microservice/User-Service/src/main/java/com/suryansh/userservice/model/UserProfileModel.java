package com.suryansh.userservice.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserProfileModel {
    private Long id;
    @NotBlank(message = "Username can't be blank")
    private String userName;
    @NotBlank(message = "Username can't be blank")
    private String nickname;
    @Valid
    private UserContactModel userContactModel;
    @Valid
    private UserAddressModel userAddressModel;

    @Data
    public static class UserContactModel {
        @Email(regexp = ".+@.+\\..+")
        private String email;
        @NotBlank(message = "Country can't be blank")
        private String country;
        @NotBlank(message = "Country Code can't be blank")
        private String countryCode;
        @Pattern(regexp = "(^$|[0-9]{10})",message = "Enter Valid Mobile Number")
        private String contact; // Changed data type to String
    }

    @Data
    public static class UserAddressModel {
        @NotBlank(message = "address line1 can't be blank")
        private String line1;
        @NotBlank(message = "address city can't be blank")
        private String city;
        @NotNull(message = "address pinCode can't be null")
        private String pinCode; // Changed data type to int
        private String otherDetails;
    }
}
