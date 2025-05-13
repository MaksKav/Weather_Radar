package com.maxkavun.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CitySearchFormDto {

        @NotBlank(message = "City name is empty")
        @Size(min = 2, max = 60, message = "The city name must be between 2 and 60 characters long")
        String cityName;

}
