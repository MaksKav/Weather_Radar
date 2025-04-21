package com.maxkavun.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {

    @NotBlank(message = "Username is empty")
    @Size(min = 5, max = 20, message = "The username must be between 5 and 20 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "The username can only contain English letters or numbers")
    private String username;

    @NotBlank(message = "Password is empty")
    @Size(min = 6, max = 20, message = "The password must be between 6 and 20 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).+$", message = "Password must contain at least one uppercase letter and one digit")
    private String password;
}

