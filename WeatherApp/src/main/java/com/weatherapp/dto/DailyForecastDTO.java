package com.weatherapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DailyForecastDTO {
    private long dt; // epoch day
    private double minTemp;
    private double maxTemp;
    private String description;
    private String icon;
}
