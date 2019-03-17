package com.management.appuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.management.appuser.repository")
public class AppUserApplication {
	public static void main(String[] args) {
		SpringApplication.run(AppUserApplication.class, args);
	}
}
