package com.nebulohub.domain.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating a new comment.
 */
public record CreateCommentDto(
        @NotBlank(message = "Comment content cannot be empty")
        @Size(max = 2000, message = "Comment is too long")
        String content,

        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Post ID is required")
        Long postId
) {
}