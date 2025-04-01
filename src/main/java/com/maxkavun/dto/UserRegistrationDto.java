package com.maxkavun.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto extends UserLoginDto{

    @NotBlank(message = "Password can't be empty")
    private String repeatPassword;
}
