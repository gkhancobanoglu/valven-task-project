package com.cobanoglu.valventask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ValvenTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(ValvenTaskApplication.class, args);
    }

}
