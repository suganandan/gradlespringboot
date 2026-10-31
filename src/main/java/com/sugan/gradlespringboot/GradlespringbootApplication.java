package com.sugan.gradlespringboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GradlespringbootApplication {

    private static final Logger log = LoggerFactory.getLogger(GradlespringbootApplication.class);

    public static void main(String[] args) {
        log.info("Starting application");
        SpringApplication.run(GradlespringbootApplication.class, args);
    }

    // TODO replace the hard-coded admin password with a secret

}
