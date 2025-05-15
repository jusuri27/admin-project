package com.example.admin_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdminProjectApplication {

	public static void main(String[] args) {
		System.out.println("args = " + args);
		SpringApplication.run(AdminProjectApplication.class, args);
	}

}
