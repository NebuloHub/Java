package com.nebulohub.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;


public record UpdateUserDto(
        @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
        String username,

        @Email(message = "Email must be valid")
        @Size(max = 255, message = "Email is too long")
        String email,

        @Size(min = 6, message = "Password must be at least 6 characters long")
        String password,

        @Size(min = 6, max = 20, message = "Role must be valid")
        String role
) {
}