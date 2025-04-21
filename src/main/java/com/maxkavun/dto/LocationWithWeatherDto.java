package com.maxkavun.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.maxkavun.serialization.WeatherByLocationDeserializer;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = WeatherByLocationDeserializer.class)
public class LocationWithWeatherDto{
        private String name;
        private String countryCode;
        private int realTemperature;
        private int temperatureFeelsLike;
        private int humidity;
        private String mainCondition;
        private String description;
        private BigDecimal latitude;
        private BigDecimal longitude;
}
