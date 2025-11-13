package com.nebulohub.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record CreateUserDto(
        @NotBlank(message = "{register.validation.username.required}")
        @Size(min = 3, max = 100, message = "{register.validation.username.size}")
        String username,

        @NotBlank(message = "{register.validation.email.required}")
        @Email(message = "{register.validation.email.valid}")
        @Size(max = 255, message = "{register.validation.email.size}")
        String email,

        @NotBlank(message = "{register.validation.password.required}")
        @Size(min = 6, message = "{register.validation.password.size}")
        String password
) {
}