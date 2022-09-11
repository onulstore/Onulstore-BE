package com.onulstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OnulstoreBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnulstoreBeApplication.class, args);
    }

}
