package com.nebulohub.domain.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * The userId will be automatically retrieved
 * from the authenticated user's token.
 */
public record CreatePostDto(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title is too long")
        String title,

        @NotBlank(message = "Description is required")
        String description
) {
}