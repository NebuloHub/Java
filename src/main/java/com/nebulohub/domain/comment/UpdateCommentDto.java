package com.nebulohub.domain.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for updating an existing comment.
 * Only the content is editable.
 */
public record UpdateCommentDto(
        @NotBlank(message = "Comment content cannot be empty")
        @Size(max = 2000, message = "Comment is too long")
        String content
) {
}