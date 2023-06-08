package com.test.weather.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Weather {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String requestedCityName;
    private String cityName;
    private String country;
    private Integer temperature;
    private Date updatedTime;
    private Date responseLocalTime;

    public Weather(String requestedCityName,
                   String cityName,
                   String country,
                   Integer temperature,
                   Date updatedTime,
                   Date responseLocalTime) {
        this.requestedCityName = requestedCityName;
        this.cityName = cityName;
        this.country = country;
        this.temperature = temperature;
        this.updatedTime = updatedTime;
        this.responseLocalTime = responseLocalTime;
    }
}
