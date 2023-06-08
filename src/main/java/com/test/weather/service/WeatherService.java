package com.test.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.weather.dto.WeatherDto;
import com.test.weather.dto.WeatherResponse;
import com.test.weather.model.Weather;
import com.test.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WeatherService {

    private static final String API_URL = "http://api.weatherstack.com/current?access_key=YOUR_API_KEY&query=";
    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public WeatherDto getWeatherByCityName(String city) {
        Optional<Weather> weatherOptional = weatherRepository.findFirstByRequestedCityNameOrderByUpdatedTimeDesc(city);

        if (weatherOptional.isEmpty()) {
            return WeatherDto.toDto(getWeatherFromWeatherStack(city));
        }

        return WeatherDto.toDto(weatherOptional.get());
    }


    private Weather getWeatherFromWeatherStack(String city) {
        ResponseEntity<String> responseEntity =  restTemplate.getForEntity(API_URL + city, String.class);

        try {
            WeatherResponse weatherResponse = objectMapper.readValue(responseEntity.getBody(), WeatherResponse.class);
            return saveWeather(city, weatherResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
