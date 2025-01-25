package com.crewing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing
@EnableScheduling
@EnableCaching
public class CrewingApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrewingApplication.class, args);
    }

}
