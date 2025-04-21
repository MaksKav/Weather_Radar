package com.maxkavun.serialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.maxkavun.dto.LocationWithWeatherDto;
import java.io.IOException;

public class WeatherByLocationDeserializer extends JsonDeserializer<LocationWithWeatherDto> {

    @Override
    public LocationWithWeatherDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String name = node.get("name").asText();
        String countryCode = node.get("sys").get("country").asText();
        int realTemperature = (int) Math.round(node.get("main").get("temp").asDouble());
        int temperatureFeelsLike = (int) Math.round(node.get("main").get("feels_like").asDouble());
        int humidity = (int) Math.round(node.get("main").get("humidity").asDouble());
        String mainCondition = node.get("weather").get(0).get("main").asText();
        String description = node.get("weather").get(0).get("description").asText();
        return   LocationWithWeatherDto.builder()
                .name(name)
                .countryCode(countryCode)
                .realTemperature(realTemperature)
                .temperatureFeelsLike(temperatureFeelsLike)
                .mainCondition(mainCondition)
                .description(description)
                .humidity(humidity)
                .build();
    }
}