package com.maxkavun.validator;

import com.maxkavun.dto.UserRegistrationDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class UserRegistrationValidator {

    public void validate(UserRegistrationDto userRegistrationDto, BindingResult bindingResult) {
        if (!userRegistrationDto.getPassword().equals(userRegistrationDto.getRepeatPassword())) {
            bindingResult.rejectValue("repeatPassword", "error.repeatPassword", "Passwords must be same");
        }
    }
}
