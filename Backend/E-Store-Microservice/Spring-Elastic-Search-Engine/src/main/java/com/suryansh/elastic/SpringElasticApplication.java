package com.suryansh.elastic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * This service is used for handling search operation in Microservice.
 * This service uses elasticsearch for storing data.
 * This service also uses Redis cache for caching and improving performance.
 *
 * @since July-15-2023.
 * @author suryansh
 */
@SpringBootApplication
@EnableCaching
public class SpringElasticApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringElasticApplication.class, args);
	}

}
