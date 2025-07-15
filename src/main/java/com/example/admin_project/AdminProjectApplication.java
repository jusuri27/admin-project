package com.example.admin_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AdminProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(AdminProjectApplication.class, args);
	}

}
