package com.hansarangdelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HansarangDeliveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(HansarangDeliveryApplication.class, args);
    }

}
