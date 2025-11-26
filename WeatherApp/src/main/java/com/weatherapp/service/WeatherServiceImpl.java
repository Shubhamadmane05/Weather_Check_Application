package com.weatherapp.service;

import com.weatherapp.dto.CurrentWeatherDTO;
import com.weatherapp.dto.DailyForecastDTO;
import com.weatherapp.dto.ForecastResponseDTO;
import com.weatherapp.dto.WeatherResponseDTO;
import com.weatherapp.entity.Weather;
import com.weatherapp.exception.CityNotFoundException;
import com.weatherapp.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService{

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${weather.api.key}")
    private String apiKey;


    @Value("${weather.api.base-url}")
    private String apiUrl;
    @Value("${weather.forecast.base-url}")
    private String forecastUrl;



    private static  final long CACHE_EXPIRY_MINUTES = 10;

    @Override
    public WeatherResponseDTO getWeatherByCity(String city) {
        String cityLower = city.toLowerCase().trim();

        Optional<Weather> cached = weatherRepository.findByCityName(cityLower);
        if(cached.isPresent() && cached.get().getUpdatedAt().plusMinutes(CACHE_EXPIRY_MINUTES).isAfter(LocalDateTime.now())){
            return convertApiResponse(cached.get().getApiResponse());
        }
        String url = apiUrl + "?q=" + cityLower + "&appid=" + apiKey + "&units=metric";


        try{
            String response=restTemplate.getForObject(url,String.class);

            Weather weather = cached.orElse(new Weather());

            weather.setCityName(cityLower);
            weather.setApiResponse(response);
            weather.setUpdatedAt(LocalDateTime.now());
            weatherRepository.save(weather);

            return  convertApiResponse(response);
        }catch (Exception e) {
        System.out.println("Error message: " + e.getMessage());
        throw new CityNotFoundException("City '" + city + "' Not Found or Unavailable");
    }

}

    @Override
    public WeatherResponseDTO convertApiResponse(String response) {

        try{
            JsonNode node = objectMapper.readTree(response);

            return  WeatherResponseDTO.builder()
                    .city(node.get("name").asText())
                    .country(node.get("sys").get("country").asText())
                    .temperature(node.get("main").get("temp").asDouble())
                    .humidity(node.get("main").get("humidity").asInt())
                    .windSpeed(node.get("wind").get("speed").asDouble())
                    .weatherDescription(node.get("weather").get(0).get("description").asText())
                    .icon(node.get("weather").get(0).get("icon").asText())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Error parsing API Response");
        }

    }

    @Override
    public List<WeatherResponseDTO> getAllCachedWeather() {
        List<Weather> weathers = weatherRepository.findAll();

        return weathers.stream()
                .map(weather -> convertApiResponse(weather.getApiResponse()))
                .toList();
    }


    @Override
    public void clearCache() {
        weatherRepository.deleteAll();
    }

    @Override
    public String checkHealth() {
        return "Weather API is running";
    }
    @Override
    public ForecastResponseDTO getForecastByCity(String city) {
        String cityLower = city.toLowerCase().trim();

        try {
            // Call FREE OpenWeather Forecast API
            String url = forecastUrl + "?q=" + cityLower + "&appid=" + apiKey + "&units=metric";
            String response = restTemplate.getForObject(url, String.class);

            JsonNode root = objectMapper.readTree(response);

            // If city not found -> API returns cod = "404"
            if (root.has("cod") && root.get("cod").asText().equals("404")) {
                throw new CityNotFoundException("City '" + city + "' Not Found");
            }

            //  Extract city info
            JsonNode cityNode = root.get("city");
            String cityName = cityNode.get("name").asText();
            String country = cityNode.get("country").asText();
            double lat = cityNode.get("coord").get("lat").asDouble();
            double lon = cityNode.get("coord").get("lon").asDouble();

            // 3) Extract forecast list (3-hour interval)
            List<DailyForecastDTO> dailyList = new ArrayList<>();

            for (JsonNode item : root.get("list")) {
                DailyForecastDTO dto = DailyForecastDTO.builder()
                        .dt(item.get("dt").asLong())
                        .minTemp(item.get("main").get("temp_min").asDouble())
                        .maxTemp(item.get("main").get("temp_max").asDouble())
                        .description(item.get("weather").get(0).get("description").asText())
                        .icon(item.get("weather").get(0).get("icon").asText())
                        .build();

                dailyList.add(dto);
            }

            // response DTO
            return ForecastResponseDTO.builder()
                    .city(cityName)
                    .country(country)
                    .lat(lat)
                    .lon(lon)
                    .daily(dailyList)
                    .build();

        } catch (CityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing forecast API: " + e.getMessage());
        }
    }
}
