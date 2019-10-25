package com.counect.cube.ocrcomparejpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OcrcompareJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(OcrcompareJpaApplication.class, args);
    }

}
