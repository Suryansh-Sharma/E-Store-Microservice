package com.suryansh.orderservice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AddressDto {
    private Long id;
    private String username;
    private String line1;
    private String city;
    private Integer pinCode;
    private String otherDetails;
}
