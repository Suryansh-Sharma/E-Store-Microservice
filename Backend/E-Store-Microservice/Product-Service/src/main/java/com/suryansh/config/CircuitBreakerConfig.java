package com.suryansh.config;

import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CircuitBreakerConfig {
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> customerServiceCusomtizer(){
        return factory->{
          factory.configure(builder->builder
                  .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(2)).build())
                  .circuitBreakerConfig(io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.ofDefaults())
                  ,"PRODUCT-SERVICE");
        };
    }
}
