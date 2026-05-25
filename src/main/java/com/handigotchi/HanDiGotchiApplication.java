package com.handigotchi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HanDiGotchiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HanDiGotchiApplication.class, args);
    }
}
