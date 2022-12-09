package com.suryansh.userservice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AddressDto {
    private Long id;
    private Long userId;
    private String line1;
    private String city;
    private int pinCode;
    private String otherDetails;
}
