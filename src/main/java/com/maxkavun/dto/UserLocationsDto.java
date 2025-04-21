package com.maxkavun.dto;

import java.util.List;

public record UserLocationsDto(String username , List<LocationWithWeatherDto> locations) {}
