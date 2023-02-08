package com.suryansh.userservice.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AddressModel {
    private Long id;
    @NotBlank(message = "Username can't be blank")
    private String userName;
    @NotBlank(message = "address line1  can't be blank")
    private String line1;
    @NotBlank(message = "address city  can't be blank")

    private String city;
    @NotNull(message = "address pinCode can't be null")
    private Integer pinCode;
    private String otherDetails;
}
