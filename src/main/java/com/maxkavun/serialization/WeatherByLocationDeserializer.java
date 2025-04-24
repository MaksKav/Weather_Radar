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

        var name = node.get("name").asText();
        var countryCode = node.get("sys").get("country").asText();
        var realTemperature = (int) Math.round(node.get("main").get("temp").asDouble());
        var temperatureFeelsLike = (int) Math.round(node.get("main").get("feels_like").asDouble());
        var humidity = (int) Math.round(node.get("main").get("humidity").asDouble());
        var mainCondition = node.get("weather").get(0).get("main").asText();
        var description = node.get("weather").get(0).get("description").asText();
        return LocationWithWeatherDto.builder()
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