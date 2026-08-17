package com.busbooking.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BusManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusManagementSystemApplication.class, args);
	}

}
