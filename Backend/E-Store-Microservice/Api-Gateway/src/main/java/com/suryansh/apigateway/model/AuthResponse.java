package com.suryansh.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String userName;
    private String email;
    private String accessToken;
    private String refreshToken;
    private long expiresAt;
    private Collection<String>authorityList;
}
