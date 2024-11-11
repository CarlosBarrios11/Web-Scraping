package com.javabuilders.demowebscraping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DemoWebScrapingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoWebScrapingApplication.class, args);
    }
}
