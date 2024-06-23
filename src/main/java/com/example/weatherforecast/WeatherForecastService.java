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
    private List<ObjectNode> extractDailyForecast(ObjectMapper mapper, JsonNode weatherData) throws RuntimeException {
        if (weatherData == null) throw new IllegalArgumentException();
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
        JsonNode dailyForecast = weatherData.findValue("forecastday");
        if (!dailyForecast.isArray()) throw new RuntimeException();
        return StreamSupport
                .stream(dailyForecast.spliterator(), false)
                .map((node) -> {
                    ObjectNode dailyNode = mapper.createObjectNode();
                    String dateString = node.findValue("date").textValue();
                    ObjectNode data = ((ObjectNode) node.findValue("day")).retain(features);
                    dailyNode.set(dateString, data);
                    return dailyNode;
                })
                .collect(Collectors.toList());
    }
    private ArrayNode getForecastFromCity(ObjectMapper mapper, String cityName, int days) throws JsonProcessingException {
        String uri = String.format(
                "https://api.weatherapi.com/v1/forecast.json?key=42aba646982448d3b1672108242306&q=%s&days=%s",
                cityName, days);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(uri, String.class);
        JsonNode weatherData = mapper.readTree(response);
        List<ObjectNode> dailyForecastList =  extractDailyForecast(mapper, weatherData);
        ArrayNode dailyForecastListNode = mapper.createArrayNode();
        if (dailyForecastList == null) throw new RuntimeException();
        for (JsonNode forecast : dailyForecastList) {
            dailyForecastListNode.add(forecast);
        }
        return dailyForecastListNode;
    }

    public ObjectNode getForecastFromCities(List<String> cityNames, int days) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode cityForecastNode = mapper.createObjectNode();
        for (String city : cityNames) {
            ArrayNode forecast = getForecastFromCity(mapper, city, days);
            cityForecastNode.set(city, forecast);
        }
        return cityForecastNode;
    }
}
