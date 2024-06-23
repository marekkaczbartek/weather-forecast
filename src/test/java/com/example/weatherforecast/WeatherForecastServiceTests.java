package com.example.weatherforecast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class WeatherForecastServiceTests {

    @Autowired
    private WeatherForecastService weatherForecastService;

    private List<String> getFieldNames(JsonNode node) {
        List<String> keys = new ArrayList<>();
        node.fieldNames().forEachRemaining(keys::add);
        return keys;
    }
    @Test
    public void testWeatherForecast() throws JsonProcessingException {
        JsonNode node = weatherForecastService.fetchDataFromCities(List.of("Warsaw"), 1);
        JsonNode forecast = node.findValue("Warsaw");
        assertNotNull(forecast);
        assertEquals(
                Arrays.asList(
                        "maxtemp_c",
                        "mintemp_c",
                        "avgtemp_c",
                        "maxwind_kph",
                        "totalprecip_mm",
                        "totalsnow_cm",
                        "avgvis_km",
                        "avghumidity",
                        "uv"),
                getFieldNames(forecast.get(0)));
    }
}
