package com.nebulohub.domain.post;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record UpdatePostDto(
        @Size(max = 255, message = "Title is too long")
        String title,

        String description,

        @Pattern(regexp = "^($|http(s?):\\/\\/.*\\.(?:jpg|jpeg|gif|png|webp))$",
                message = "{post.validation.imageUrl}")
        String imageUrl
) {
}