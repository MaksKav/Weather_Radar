package com.maxkavun.service;

import com.maxkavun.dto.SessionDto;
import com.maxkavun.entity.Session;
import com.maxkavun.entity.User;
import com.maxkavun.exception.*;
import com.maxkavun.repository.SessionRepository;
import com.maxkavun.repository.UserRepository;
import com.maxkavun.util.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AuthorizationService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public AuthorizationService(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }


    @Transactional
    public UUID createSessionForUser(User user) {
        try {
            var sessionId = UUID.randomUUID();
            var sessionTimeLife = 15;
            var expirationTime = LocalDateTime.now().plusMinutes(sessionTimeLife);
            sessionRepository.save(new Session(sessionId, user, expirationTime));
            log.info("Session with id {} created successfully", sessionId);
            return sessionId;
        } catch (RepositoryException e) {
            log.error("Error while creating session for user: {}", user.getLogin(), e);
            throw new AuthorizationServiceException("Failed to create session", e);
        }
    }


    @Transactional
    public Optional<UUID> saveSessionIfUserAuthorized(String username, String password) {
        try {
            var userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) {
                throw new NotFoundException("User not found");
            }

            return userOpt.map(user -> {
                if (!PasswordUtils.checkPassword(password, user.getPassword())) {
                    log.warn("Incorrect password for user {}", username);
                    throw new IncorrectPasswordException("Password is not correct");
                }
                return createSessionForUser(user);
            });
        } catch (RepositoryException e) {
            log.error("Database error while authorizing user {}", username, e);
            throw new AuthorizationServiceException("Database error occurred", e);
        }
    }


    @Transactional(readOnly = true)
    public boolean isSessionValid(String sessionId) {
        var sessionUuid = UUID.fromString(sessionId);
        return sessionRepository.findById(sessionUuid).map(session -> session.getExpiresAt().isAfter(LocalDateTime.now())).orElse(false);
    }


    @Transactional(readOnly = true)
    public SessionDto getSessionDto(UUID sessionId) {
        try {
            return sessionRepository.findById(sessionId)
                    .map(session -> new SessionDto(session.getId(), session.getExpiresAt()))
                    .orElseThrow(() -> new NotFoundException("Session not found with id: " + sessionId));
        } catch (RepositoryException e) {
            log.error("Database error while finding session with id: {}", sessionId);
            throw new AuthorizationServiceException("Database error occurred", e);
        }
    }


    @Transactional()
    public void changeExpireTimeInSession(String sessionId, LocalDateTime now) {
        var sessionUuid = UUID.fromString(sessionId);

        var sessionOpt = sessionRepository.findById(sessionUuid);
        sessionOpt.ifPresent(session -> {
            session.setExpiresAt(now);
            sessionRepository.save(session);
        });
    }
}
