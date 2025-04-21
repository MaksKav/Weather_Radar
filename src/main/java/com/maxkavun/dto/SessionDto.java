package com.maxkavun.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessionDto(UUID sessionId , LocalDateTime expiresAt) {}
