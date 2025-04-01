package com.maxkavun.service;

import com.maxkavun.entity.User;
import com.maxkavun.exception.RegistrationServiceException;
import com.maxkavun.exception.RepositoryException;
import com.maxkavun.exception.UserPersistenceException;
import com.maxkavun.repository.UserRepository;
import com.maxkavun.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegistrationService {

    private final UserRepository userRepository;

    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean saveUserIfNotExists(String username , String password) {
        try {
            if (userRepository.findByUsername(username).isPresent()) {
                log.info("Couldn't save user because user already exists: {}" , username);
                return false;
            }else{
                String hashedPassword = PasswordUtil.generatePassword(password);
                userRepository.save(User.builder()
                        .login(username)
                        .password(hashedPassword)
                        .build());
                log.info("Saved user successfully : {}", username);
                return true;
            }
        }catch (UserPersistenceException | RepositoryException exception){
            log.error("Error while saving user: {}",username, exception);
            throw new RegistrationServiceException("Failed to save new user" , exception);
        }
    }
}
