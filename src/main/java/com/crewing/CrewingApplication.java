package com.crewing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class CrewingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrewingApplication.class, args);
    }

}
