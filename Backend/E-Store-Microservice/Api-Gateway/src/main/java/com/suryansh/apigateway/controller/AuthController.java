package com.suryansh.apigateway.controller;

import com.suryansh.apigateway.model.AuthResponse;
import com.suryansh.apigateway.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/authenticate")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @GetMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @AuthenticationPrincipal OidcUser oidcUser,
            Model model,
            @RegisteredOAuth2AuthorizedClient("okta")
            OAuth2AuthorizedClient client
    ){
        AuthResponse authResponse
                = AuthResponse.builder()
                .userName(oidcUser.getName())
                .email(oidcUser.getEmail())
                .accessToken(client.getAccessToken().getTokenValue())
                .refreshToken(client.getRefreshToken().getTokenValue())
                .expiresAt(client.getAccessToken().getExpiresAt().getEpochSecond())
                .authorityList(oidcUser.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .build();
        addUserInDatabase(client.getAccessToken().getTokenValue(),oidcUser.getEmail());
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
    @Async
    public void addUserInDatabase(String accessToken, String email) {
        authService.saveUserInDataBase(email,accessToken);
    }


}
