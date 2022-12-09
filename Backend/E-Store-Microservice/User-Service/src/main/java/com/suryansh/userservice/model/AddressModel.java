package com.suryansh.userservice.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AddressModel {
    private Long id;
    private String userName;
    private String line1;
    private String city;
    private int pinCode;
    private String otherDetails;
}
