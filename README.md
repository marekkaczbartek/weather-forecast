## Weather Forecast API

### Description

This app is a RESTful extension for a weather API available under
www.weatherapi.com. It is written using Spring Boot framework with 
Java 21, tested using JUnit5 and documented with OpenAPI 3.0 and SwaggerUI.

### Usage

The app can be used to forecast weather in Poland's 5 largest cities:
Warsaw, Cracow, Wroclaw, Lodz and Poznan. The weather can be predicted 
for a given number of days into the future, and is represented by following
features (as measured daily):
- `maxtemp_c` - maximum temperature (in celsius),
- `mintemp_c` - minimum temperature (in celsius),
- `avgtemp_c` - average temperature (in celsius),
- `maxwind_kph` - maximum wind speed (in kilometers per hour),
- `totalprecip_mm` - total precipitation (in millimeters),
- `totalsnow_cm` - total snowfall (in centimeters),
- `avgvis_km` - average visibility (in kilometers),
- `avghumidity` - average humidity as a percentage,
- `uv` - uv index

### Endpoints

The app runs on `http://localhost:8080`

1. `/forecast/params?days={days}` - returns a weather forecast 
for a given number of days into the future

2. `/forecast` - returns a weather forecast for the next 3 days

3. `/swagger-ui/index.html` - OpenAPI/Swagger documentation