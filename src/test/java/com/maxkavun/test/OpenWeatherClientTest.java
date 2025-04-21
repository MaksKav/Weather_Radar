package com.maxkavun.test;

import com.maxkavun.service.client.OpenWeatherClient;
import com.maxkavun.config.AppConfig;
import com.maxkavun.dto.LocationInfoDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;


import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {AppConfig.class})
public class OpenWeatherClientTest {

    @Autowired
    private OpenWeatherClient client;

    @Test
    public void testListLocationsFromCityName() {
        String cityName = "Warsaw";
        List<LocationInfoDto> cities = client.getLocationsByCityName(cityName);
        assertAll(
                () -> assertFalse(cities.isEmpty()),
                () -> {
                    for (LocationInfoDto city : cities) {
                        assertTrue(city.latitude().scale() <= 6);
                        assertTrue(city.longitude().scale() <= 6);
                        assertFalse(city.country().isBlank());
                    }
                }
        );
    }

    @Test
    public void testLocationFromCoordinates() {
        String cityName = "Warsaw";
        String cityCode = "PL";
        BigDecimal lat = BigDecimal.valueOf(52.231958);
        BigDecimal lon = BigDecimal.valueOf(21.006724);

        var location = client.getLocationByCoordinates(lat, lon);
        assertTrue(location.isPresent(), "location is not present");
        var city = location.get();
        assertAll(
                () -> assertEquals(city.getCountryCode(), cityCode),
                () -> assertEquals(city.getName(), cityName),
                () -> {
                    BigDecimal actual = BigDecimal.valueOf(city.getRealTemperature());
                    BigDecimal feelsLike = BigDecimal.valueOf(city.getTemperatureFeelsLike());
                    BigDecimal diff = actual.subtract(feelsLike).abs();
                    BigDecimal normalDifference = BigDecimal.valueOf(20);
                    assertTrue(diff.compareTo(normalDifference) <= 0,
                            "Difference between actualTemp and feelsLikeTemp is greater than 20: " + diff);
                },
                () -> {
                    int minTem = -60;
                    int maxTemp = 60;
                    assertTrue(city.getRealTemperature() < maxTemp && city.getRealTemperature() > minTem);
                }
        );
    }
}
