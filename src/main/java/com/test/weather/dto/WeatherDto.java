package com.test.weather.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.test.weather.model.Weather;

import java.util.Date;

public record WeatherDto(
        String cityName,
        String country,
        Integer temperature,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        Date updatedTime
) {
    public static WeatherDto toDto(Weather from) {
        return new WeatherDto(
                from.getCityName(),
                from.getCountry(),
                from.getTemperature(),
                from.getUpdatedTime()
        );
    }
}
