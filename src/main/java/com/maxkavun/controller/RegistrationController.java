package com.maxkavun.controller;

import com.maxkavun.dto.UserRegistrationDto;
import com.maxkavun.exception.UserAlreadyExistsException;
import com.maxkavun.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Slf4j
@Controller
public class RegistrationController {

    private final RegistrationService registrationService;


    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "registration";
    }

    @PostMapping("/process-registration")
    public String processRegistration(@Valid @ModelAttribute("userRegistrationDto") UserRegistrationDto userRegistrationDto, BindingResult bindingResult) {
        if (!userRegistrationDto.getPassword().equals(userRegistrationDto.getRepeatPassword())) {
            bindingResult.rejectValue("repeatPassword", "error.repeatPassword", "Passwords must be same");
            return "registration";
        }

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        try {
            boolean isUserNotExists = registrationService.saveUserIfNotExists(userRegistrationDto.getUsername(), userRegistrationDto.getPassword());
            if (isUserNotExists) {
                log.info("User: {} successfully saved in the DB , return to login", userRegistrationDto.getUsername());
                return "redirect:/";
            }
        } catch (UserAlreadyExistsException exception) {
            bindingResult.rejectValue("repeatPassword", "error.userExists", "User already exists , please return to login page");
            return "registration";
        }

        return "error";
    }
}
