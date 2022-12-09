package com.suryansh.userservice.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String line1;
    private String city;
    private int pinCode;
    private String otherDetails;

}
