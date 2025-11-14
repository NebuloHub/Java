package com.nebulohub.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern; // <-- IMPORT ADICIONADO
import jakarta.validation.constraints.Size;


public record UpdateUserDto(
        @Size(min = 3, max = 100, message = "{user.validation.username.size}")
        String username,

        @Email(message = "{user.validation.email.valid}")
        @Size(max = 255, message = "{user.validation.email.size}")
        String email,

        @Pattern(regexp = "^($|.{6,})$", message = "{user.validation.password.size}")
        String password,

        @Size(min = 6, max = 20, message = "{user.validation.role.size}")
        String role
) {
}