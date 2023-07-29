package com.test.weather.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.weather.constants.Constants;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
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

    //Spring Caching
    @Bean
    public CacheManager getCacheManager() {
        return new ConcurrentMapCacheManager(Constants.CACHE_NAME);
    }
}
