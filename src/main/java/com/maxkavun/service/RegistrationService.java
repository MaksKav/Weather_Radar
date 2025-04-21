package com.maxkavun.service;

import com.maxkavun.entity.User;
import com.maxkavun.exception.*;
import com.maxkavun.repository.UserRepository;
import com.maxkavun.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class RegistrationService {

    private final UserRepository userRepository;

    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean saveUserIfNotExists(String username, String password) {
        try {
            if (userRepository.findByUsername(username).isPresent()) {
                log.warn("Couldn't save user because user already exists: {}", username);
                throw new UserAlreadyExistsException("User with username: " + username + " already exists");
            } else {
                String hashedPassword = PasswordUtil.generatePassword(password);
                userRepository.save(User.builder()
                        .login(username)
                        .password(hashedPassword)
                        .build());
                log.info("Saved user successfully : {}", username);
                return true;
            }
        } catch (UserPersistenceException | RepositoryException exception) {
            log.error("Error while saving user: {}", username);
            throw new RegistrationServiceException("Failed to save new user", exception);
        }
    }
}
