package com.example.weatherforecast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class WeatherForecastService {
    private List<JsonNode> extractDailyForecast(JsonNode jsonNode) {
        JsonNode arrNode = jsonNode.findValue("forecastday");
        if (!arrNode.isArray()) return null;
        return StreamSupport
                .stream(arrNode.spliterator(), false)
                .map((node) -> node.findValue("day"))
                .collect(Collectors.toList());
    }
    public ArrayNode fetchDataFromCity(String cityName, int days) throws JsonProcessingException {
        String uri = String.format("https://api.weatherapi.com/v1/forecast.json?key=42aba646982448d3b1672108242306&q=%s&days=%s", cityName, days);
        RestTemplate restTemplate = new RestTemplate();
        String res = restTemplate.getForObject(uri, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(res);
        ArrayNode arrayNode = mapper.createArrayNode();
        List<JsonNode> forecastList =  extractDailyForecast(jsonNode);
        assert forecastList != null;
        for (JsonNode forecast : forecastList) {
            arrayNode.add(forecast);
        }
        return arrayNode;
    }
}
