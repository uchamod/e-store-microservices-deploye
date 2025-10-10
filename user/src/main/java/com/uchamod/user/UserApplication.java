package com.uchamod.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

//this is user application

@SpringBootApplication
@EnableConfigurationProperties
@EnableEurekaClient
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

@EventListener(ApplicationReadyEvent.class)
public void applicationReady() {
	System.out.println("EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: " +
							   System.getenv("EUREKA_CLIENT_SERVICEURL_DEFAULTZONE"));
	System.out.println("SPRING_PROFILES_ACTIVE: " +
							   System.getenv("SPRING_PROFILES_ACTIVE"));
}

}
