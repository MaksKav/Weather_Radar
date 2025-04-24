package com.maxkavun.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxkavun.dto.LocationInfoDto;
import com.maxkavun.dto.LocationWithWeatherDto;
import com.maxkavun.exception.HttpClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OpenWeatherClient {

    public static final String GEO_LOCATIONS_URL = "https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=10&appid=%s";
    public static final String WEATHER_BY_COORDINATES_URL = "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&appid=%s";
    public static final int TIMEOUT_SECONDS = 5;

    @Value("${openweather.api.key}")
    private String apiKey;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper;


    public List<LocationInfoDto> getLocationsByCityName(String cityName){
        try {
            var encodedCityName = URLEncoder.encode(cityName, StandardCharsets.UTF_8);
            var url = String.format(GEO_LOCATIONS_URL, encodedCityName, apiKey);

            var request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .version(HttpClient.Version.HTTP_1_1)
                    .timeout(Duration.of(TIMEOUT_SECONDS , ChronoUnit.SECONDS))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            var type = objectMapper.getTypeFactory().constructCollectionType(List.class, LocationInfoDto.class);
            return objectMapper.readValue(response.body(), type);
        } catch (Exception e) {
           throw new HttpClientException("Error getting locations from OpenWeather API", e);
        }
    }

    public Optional<LocationWithWeatherDto> getLocationByCoordinates(BigDecimal latitude, BigDecimal longitude){
        var url = String.format(WEATHER_BY_COORDINATES_URL , latitude, longitude, apiKey);
        try {
            var request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .version(HttpClient.Version.HTTP_1_1)
                    .timeout(Duration.of(TIMEOUT_SECONDS , ChronoUnit.SECONDS))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request , HttpResponse.BodyHandlers.ofString());

            return Optional.of(objectMapper.readValue(response.body(), LocationWithWeatherDto.class));
        } catch (Exception e) {
            throw  new HttpClientException("Error getting location by coordinates from OpenWeather API", e);
        }
    }
}
