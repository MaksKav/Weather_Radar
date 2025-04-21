package com.maxkavun.dto;

import java.util.List;

public record UserLocationsWithWeatherDto(String username , List<LocationWithWeatherDto> locations) {}
