package com.suryansh.userservice.entity;

import jakarta.persistence.*;
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
    private String line1;
    private String city;
    private String pinCode;
    private String otherDetails;

    @OneToOne
    private User user;
}
