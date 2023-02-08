package com.suryansh.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${auth0.audience}")
    private String audience;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((requests) -> requests
                .antMatchers(
                        "/api/user/getUserAddress/{userName}",
                        "/api/user/isProductLikedByUser/{userName}/{productId}",
                        "/api/user/likedProducts-byUser/{userName}",
                        "/api/user/updateUserAddress",
                        "/api/user/addUserAddress",
                        "/api/user/likeProduct",
                        "/api/user/unLikeProduct",
                        "/api/user/by-username/{username}")
                .authenticated()
                .anyRequest()
                .permitAll());
        http.authorizeHttpRequests()
                .and()
                .cors(Customizer.withDefaults())
                .oauth2ResourceServer()
                .jwt();
        return http.build();

    }

    @Bean
    JwtDecoder jwtDecoder() {
        String issuer = "https://dev-kx0i75g5w8igcuhs.us.auth0.com/";
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(issuer);

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }
}
