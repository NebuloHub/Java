package com.nebulohub.domain.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * The userId will be automatically retrieved
 * from the authenticated user's token.
 */
public record CreateCommentDto(
        @NotBlank(message = "Comment content cannot be empty")
        @Size(max = 2000, message = "Comment is too long")
        String content,

        @NotNull(message = "Post ID is required")
        Long postId
) {
}