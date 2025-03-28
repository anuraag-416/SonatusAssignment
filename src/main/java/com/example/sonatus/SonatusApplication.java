package com.example.sonatus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SonatusApplication {

    public static void main(String[] args) {
        SpringApplication.run(SonatusApplication.class, args);
    }
}
