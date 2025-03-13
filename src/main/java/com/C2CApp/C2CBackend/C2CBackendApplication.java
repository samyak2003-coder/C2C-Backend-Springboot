package com.C2CApp.C2CBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class C2CBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(C2CBackendApplication.class, args);
	}
}
