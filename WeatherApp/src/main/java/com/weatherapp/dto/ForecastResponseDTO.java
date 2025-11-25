package com.weatherapp.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ForecastResponseDTO {
    private String city;
    private String country;
    private double lat;
    private double lon;
    private CurrentWeatherDTO current;
    private List<DailyForecastDTO> daily;
}

