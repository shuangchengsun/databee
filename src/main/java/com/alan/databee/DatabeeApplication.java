package com.alan.databee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DatabeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabeeApplication.class, args);
    }

}
