package com.bosanci.recipesbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RecipesBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipesBackendApplication.class, args);
	}

}