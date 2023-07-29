package com.test.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.weather.constants.Constants;
import com.test.weather.dto.WeatherDto;
import com.test.weather.dto.WeatherResponse;
import com.test.weather.model.Weather;
import com.test.weather.repository.WeatherRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@CacheConfig(cacheNames = {Constants.CACHE_NAME})
@Service
public class WeatherService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Cacheable(key = "#city")
    public WeatherDto getWeatherByCityName(String city) {
        logger.info("Requested city: " + city);
        Optional<Weather> weatherOptional = weatherRepository.findFirstByRequestedCityNameOrderByUpdatedTimeDesc(city);

        return weatherOptional.map(weather -> {
            if (weather.getUpdatedTime().before(new Date(System.currentTimeMillis() + 1800000))) {
                return WeatherDto.toDto(getWeatherFromWeatherStack(city));
            }
            return WeatherDto.toDto(weather);
        }).orElseGet(() -> WeatherDto.toDto(getWeatherFromWeatherStack(city)));
    }

    @CacheEvict(allEntries = true)
    @PostConstruct
    @Scheduled(fixedDelayString = "10000")
    public void clearCache() {
        logger.info("Cache cleared");
    }

    private Weather getWeatherFromWeatherStack(String city) {
        ResponseEntity<String> responseEntity =  restTemplate.getForEntity(getWeatherStackUrl(city), String.class);

        try {
            WeatherResponse weatherResponse = objectMapper.readValue(responseEntity.getBody(), WeatherResponse.class);
            return saveWeather(city, weatherResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getWeatherStackUrl(String city) {
        return Constants.API_URL + Constants.ACCESS_KEY_PARAM + Constants.API_KEY + Constants.QUERY_KEY_PARAM + city;
    }

    private Weather saveWeather(String city, WeatherResponse weatherResponse) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDate localDate = LocalDate.parse(weatherResponse.location().localtime(), formatter);
        Weather weather = new Weather(
                city,
                weatherResponse.location().name(),
                weatherResponse.location().country(),
                weatherResponse.current().temperature(),
                new Date(),
                java.sql.Date.valueOf(localDate)
        );

        return weatherRepository.save(weather);
    }
}
