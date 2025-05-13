package com.maxkavun.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto extends UserLoginDto{

    @NotBlank(message = "Password can't be empty")
    private String repeatPassword;
}
