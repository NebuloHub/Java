package com.nebulohub.domain.comment;

import com.nebulohub.domain.comment.Comment;

import java.time.LocalDateTime;

/**
 * DTO for sending comment data back to the client.
 * Includes simplified author info.
 */
public record ReadCommentDto(
        Long id,
        String content,
        LocalDateTime createdAt,
        Long userId,
        String username,
        Long postId
) {
    /**
     * Convenience constructor to map a Comment entity to this DTO.
     */
    public ReadCommentDto(Comment comment) {
        this(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUser() != null ? comment.getUser().getId() : null,
                comment.getUser() != null ? comment.getUser().getActualUsername() : null,
                comment.getPost() != null ? comment.getPost().getId() : null
        );
    }
}