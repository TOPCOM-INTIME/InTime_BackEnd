package com.topcom.intime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class IntimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntimeApplication.class, args);
	}

}
