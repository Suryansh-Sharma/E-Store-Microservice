package com.suryansh.apigateway.service;

public interface AuthService {
    void saveUserInDataBase(String email, String accessToken);
}
