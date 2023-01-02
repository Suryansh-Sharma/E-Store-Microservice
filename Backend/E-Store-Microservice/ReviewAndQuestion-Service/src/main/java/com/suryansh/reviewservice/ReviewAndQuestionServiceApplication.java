package com.suryansh.reviewservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ReviewAndQuestionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewAndQuestionServiceApplication.class, args);
	}

}
