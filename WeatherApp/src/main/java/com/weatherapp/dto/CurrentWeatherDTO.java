package com.weatherapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrentWeatherDTO {
    private double temp;
    private int humidity;
    private double windSpeed;
    private String description;
    private String icon;
    private long sunrise;
    private long sunset;
}

