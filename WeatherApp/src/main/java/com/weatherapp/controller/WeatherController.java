package com.weatherapp.controller;



import com.weatherapp.dto.ForecastResponseDTO;
import com.weatherapp.dto.WeatherResponseDTO;
import com.weatherapp.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@CrossOrigin("*")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/msg")
    public String getMessage()
    {
        return "Welcome to App";
    }

    @GetMapping
    public ResponseEntity<WeatherResponseDTO> getWeather(@RequestParam String city){

        return new ResponseEntity<>(weatherService.getWeatherByCity(city), HttpStatus.OK);

    }


    @GetMapping("/cached")
    public ResponseEntity<?> getCachedData() {
        return ResponseEntity.ok(weatherService.getAllCachedWeather());
    }

    @DeleteMapping("/cache/clear")
    public ResponseEntity<String> clearCache() {
        weatherService.clearCache();
        return ResponseEntity.ok("Cache cleared successfully");
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok(weatherService.checkHealth());
    }

    @GetMapping("/forecast")
    public ResponseEntity<ForecastResponseDTO> getForecast(@RequestParam String city) {
        ForecastResponseDTO dto = weatherService.getForecastByCity(city);
        return ResponseEntity.ok(dto);
    }

}
