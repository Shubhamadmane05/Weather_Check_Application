package com.weatherapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherResponseDTO {

    private String city;
    private String country;
    private double temperature;
    private String weatherDescription;
    private int humidity;
    private double windSpeed;
    private String icon;
}
