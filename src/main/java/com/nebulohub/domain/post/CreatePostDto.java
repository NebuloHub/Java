package com.nebulohub.domain.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record CreatePostDto(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title is too long")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "User ID is required")
        Long userId
) {
}