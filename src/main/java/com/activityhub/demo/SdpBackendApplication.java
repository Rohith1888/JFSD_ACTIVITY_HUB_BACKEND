package com.activityhub.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
	    "com.activityhub.controller",
	    "com.activityhub.dao",
	    "com.activityhub.demo"
	})
public class SdpBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SdpBackendApplication.class, args);
	}

}
