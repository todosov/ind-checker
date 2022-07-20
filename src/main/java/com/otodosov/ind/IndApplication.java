package com.otodosov.ind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IndApplication {

	public static void main(String[] args) {
		SpringApplication.run(IndApplication.class, args);
	}

}
