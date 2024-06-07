package com.geosys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BIMApplication {

    public static void main(String[] args) {
        SpringApplication.run(BIMApplication.class, args);
    }

}
