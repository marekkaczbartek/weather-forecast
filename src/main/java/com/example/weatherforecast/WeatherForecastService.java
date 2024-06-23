package com.example.weatherforecast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class WeatherForecastService {
    private List<ObjectNode> extractDailyForecast(ObjectMapper mapper, JsonNode jsonNode) throws RuntimeException {
        if (jsonNode == null) throw new IllegalArgumentException();
        List<String> features = Arrays.asList(
                "maxtemp_c",
                "mintemp_c",
                "avgtemp_c",
                "maxwind_kph",
                "totalprecip_mm",
                "totalsnow_cm",
                "avgvis_km",
                "avghumidity",
                "uv");
        JsonNode arrNode = jsonNode.findValue("forecastday");
        if (!arrNode.isArray()) return null;
        return StreamSupport
                .stream(arrNode.spliterator(), false)
                .map((node) -> {
                    ObjectNode date = mapper.createObjectNode();
                    String dateString = node.findValue("date").textValue();
                    ObjectNode data = ((ObjectNode) node.findValue("day")).retain(features);
                    date.set(dateString, data);
                    return date;
                })
                .collect(Collectors.toList());
    }
    private ArrayNode fetchDataFromCity(ObjectMapper mapper, String cityName, int days) throws JsonProcessingException {
        String uri = String.format(
                "https://api.weatherapi.com/v1/forecast.json?key=42aba646982448d3b1672108242306&q=%s&days=%s",
                cityName, days);
        RestTemplate restTemplate = new RestTemplate();
        String res = restTemplate.getForObject(uri, String.class);
        JsonNode jsonNode = mapper.readTree(res);
        ArrayNode arrayNode = mapper.createArrayNode();
        List<ObjectNode> forecastList =  extractDailyForecast(mapper, jsonNode);
        assert forecastList != null;
        for (JsonNode forecast : forecastList) {
            arrayNode.add(forecast);
        }
        return arrayNode;
    }

    public ObjectNode fetchDataFromCities(ObjectMapper mapper, List<String> cityNames, int days) throws JsonProcessingException {
        ObjectNode objectNode = mapper.createObjectNode();
        for (String city : cityNames) {
            ArrayNode nodeList = fetchDataFromCity(mapper, city, days);
            objectNode.set(city, nodeList);
        }
        return objectNode;
    }
}
