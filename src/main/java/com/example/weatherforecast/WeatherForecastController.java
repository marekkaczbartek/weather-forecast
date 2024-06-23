package com.example.weatherforecast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@RestController
public class WeatherForecastController {
    private WeatherForecastService weatherForecastService;
    @GetMapping("/forecast/params")
    public ObjectNode getForecast(
            @RequestParam int days) throws JsonProcessingException {
        List<String> cityNames = Arrays.asList("Warsaw", "Cracow", "Wroclaw", "Lodz", "Poznan");
        return weatherForecastService.getForecastFromCities(cityNames, days);
    }

    @GetMapping("/forecast")
    public ObjectNode getForecast() throws JsonProcessingException {
        List<String> cityNames = Arrays.asList("Warsaw", "Cracow", "Wroclaw", "Lodz", "Poznan");
        return weatherForecastService.getForecastFromCities(cityNames, 3);
    }
}
