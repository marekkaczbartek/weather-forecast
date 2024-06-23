package com.example.weatherforecast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@RestController
public class WeatherForecastController {
    private WeatherForecastService weatherForecastService;
    @GetMapping("/forecast")
    public Object getData(@RequestParam int days) throws JsonProcessingException {
        List<String> cityNamesList = Arrays.asList("Warsaw", "Cracow", "Wroclaw", "Lodz", "Poznan");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        for (String city : cityNamesList) {
            ArrayNode nodeList = weatherForecastService.fetchDataFromCity(city, days);
            objectNode.set(city, nodeList);
        }
        return objectNode;
    }
}
