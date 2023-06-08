package com.test.weather.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Configs {

    // Rest Template
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    // Object Mapper
    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}
