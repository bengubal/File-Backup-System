package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
 class FileManagementSystemApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(FileManagementSystemApplication.class, args);
	}
	
	
}