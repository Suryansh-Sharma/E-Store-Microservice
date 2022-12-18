package com.suryansh.apigateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{
    private final WebClient.Builder webClientBuilder;
    @Override
    @Async
    public void saveUserInDataBase(String email, String accessToken) {
        webClientBuilder.build().get()
                .uri("http://geekyprogrammer:8080/api/user/isUserPresent/"+email)
                .header("Authorization","Bearer "+accessToken)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }
}
