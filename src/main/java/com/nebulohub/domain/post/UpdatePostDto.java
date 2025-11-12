package com.nebulohub.domain.post;

import jakarta.validation.constraints.Size;

/**
 * DTO for partially updating a post.
 * Only title and description can be updated.
 */
public record UpdatePostDto(
        @Size(max = 255, message = "Title is too long")
        String title,

        String description
) {
}