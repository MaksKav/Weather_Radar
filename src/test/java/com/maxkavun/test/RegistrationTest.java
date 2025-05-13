package com.maxkavun.test;

import com.maxkavun.config.AppConfig;
import com.maxkavun.controller.RegistrationController;
import com.maxkavun.dto.UserRegistrationDto;
import com.maxkavun.repository.UserRepository;
import com.maxkavun.service.RegistrationService;
import com.maxkavun.test.config.HibernateTestConfig;
import org.junit.jupiter.api.Assertions;
import com.maxkavun.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.Mockito.mock;


@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {AppConfig.class, HibernateTestConfig.class})
@Testcontainers
public class RegistrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private RegistrationController registrationController;

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    @Transactional
    public void userRegistrationTest(int runNumber) {
        var user = getUserDto();

        var bindingResult = mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);

        if (runNumber == 1) {
            var viewName = registrationController.processRegistration(user, bindingResult);
            var savedUser = userRepository.findByUsername(user.getUsername()).orElse(null);

            Assertions.assertNotNull(savedUser, "User should be saved in the database");
            Assertions.assertNotEquals(savedUser.getPassword(), user.getPassword(), "Password must be hashed");
            Assertions.assertEquals("redirect:/", viewName, "After successful registration, it should redirect to the home page");
        } else {
            registrationService.saveUserIfNotExists(user.getUsername(), user.getPassword());
            var exception = Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
                registrationService.saveUserIfNotExists(user.getUsername(), user.getPassword());
            });
            Assertions.assertTrue(exception.getMessage().contains("User with username"), "Expected exception message to mention user already exists");
        }
    }


    private UserRegistrationDto getUserDto() {
        var username = "testuser";
        var password = "Password_12";
        var repeatPassword = "Password_12";

        var user = new UserRegistrationDto();
        user.setUsername(username);
        user.setPassword(password);
        user.setRepeatPassword(repeatPassword);
        return user;
    }
}