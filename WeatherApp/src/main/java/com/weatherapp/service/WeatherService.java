package com.weatherapp.service;

import com.weatherapp.dto.ForecastResponseDTO;
import com.weatherapp.dto.WeatherResponseDTO;

import java.util.List;

public interface WeatherService {

    WeatherResponseDTO getWeatherByCity(String city);

    WeatherResponseDTO convertApiResponse(String response);

    List<WeatherResponseDTO> getAllCachedWeather(); // new method

    public void clearCache();

    public String checkHealth();

    ForecastResponseDTO getForecastByCity(String city);
}
