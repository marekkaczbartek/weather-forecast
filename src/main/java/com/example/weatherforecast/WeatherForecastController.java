package com.example.weatherforecast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
    public ObjectNode getData(@RequestParam int days, @RequestBody List<String> cityNamesList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return weatherForecastService.fetchDataFromCities(mapper, cityNamesList, days);
    }

    @GetMapping("/forecast")
    public ObjectNode getData() throws JsonProcessingException {
        List<String> cityNames = Arrays.asList("Warsaw", "Cracow", "Wroclaw", "Lodz", "Poznan");
        return getData( 3, cityNames);
    }
}
