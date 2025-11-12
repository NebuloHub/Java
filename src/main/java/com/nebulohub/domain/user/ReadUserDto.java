package com.nebulohub.domain.user;

import com.nebulohub.domain.user.User;

import java.time.LocalDateTime;


public record ReadUserDto(
        Long id,
        String username,
        String email,
        String role,
        LocalDateTime createdAt
) {

    public ReadUserDto(User user) {
        this(
                user.getId(),
                user.getActualUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}