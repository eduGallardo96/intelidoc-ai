package com.intelidoc.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class IntelidocBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntelidocBackendApplication.class, args);
    }
}
