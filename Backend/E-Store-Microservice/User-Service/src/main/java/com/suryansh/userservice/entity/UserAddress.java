package com.suryansh.userservice.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserAddress {
    @Id
    private Long id;
    private String line1;
    private String city;
    private int pinCode;
    private String otherDetails;

}
