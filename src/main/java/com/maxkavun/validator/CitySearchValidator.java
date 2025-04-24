package com.maxkavun.validator;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CitySearchValidator {

    public Optional<String> validate(String cityName) {
        var minSize = 2;
        var maxSize = 60;

        if (cityName == null || cityName.isBlank()) {
            return Optional.of("City name cannot be empty");
        }

        if (cityName.length() < minSize || cityName.length() > maxSize) {
            return Optional.of("The city name must contain between 2 and 60 characters");
        }

        if (cityName.matches(".*\\d.*")) {
            return Optional.of("The city name must not contain numbers");
        }

        return Optional.empty();
    }
}