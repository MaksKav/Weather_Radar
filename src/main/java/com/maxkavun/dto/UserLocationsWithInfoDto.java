package com.maxkavun.dto;

import java.util.List;

public record UserLocationsWithInfoDto (String username , List<LocationInfoDto> locations) { }
