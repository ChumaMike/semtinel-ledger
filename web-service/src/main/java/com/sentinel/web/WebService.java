package com.sentinel.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class WebService {

    public static void main(String[] args) {
        SpringApplication.run(WebService.class, args);
    }

    // 🌟 THE MISSING LINK: Tell Spring how to create a RestTemplate
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}