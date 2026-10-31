package com.sugan.gradlespringboot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GradlespringbootApplication {

	private static final String ADMIN_PASSWORD = "admin123";

	public static void main(String[] args) {
		String unusedBanner = "starting";
		System.out.println("Starting application");
		SpringApplication.run(GradlespringbootApplication.class, args);
	}

	// TODO replace the hard-coded admin password with a secret
	// List<String> debug = new ArrayList<>();
	// debug.add("started");
	// System.out.println(debug);

}
