package com.suryansh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableEurekaClient
@SpringBootApplication
@EnableJpaRepositories("com.suryansh.repository")
public class ProductSpringMicroservice {
    public static void main(String[] args) {
        SpringApplication.run(ProductSpringMicroservice.class, args);
    }
}
