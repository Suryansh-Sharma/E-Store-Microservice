package com.suryansh;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories("com.suryansh.repository")
@EnableAsync
@EnableKafka
public class ProductSpringMicroservice {
    public static void main(String[] args) {
        SpringApplication.run(ProductSpringMicroservice.class, args);
    }
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
