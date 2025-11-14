package com.nebulohub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NebulohubApplication {

	public static void main(String[] args) {
		SpringApplication.run(NebulohubApplication.class, args);
	}

}