package com.maxkavun.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.maxkavun.serialization.BigDecimal6Deserializer;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LocationInfoDto(
        String name,
        @JsonDeserialize(using = BigDecimal6Deserializer.class) @JsonProperty("lat") BigDecimal latitude,
        @JsonDeserialize(using = BigDecimal6Deserializer.class) @JsonProperty("lon") BigDecimal longitude,
        String country,
        @JsonProperty("state") String region
) {
}

