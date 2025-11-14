package com.nebulohub.domain.post.dto;

import com.nebulohub.domain.comment.ReadCommentDto;
import com.nebulohub.domain.post.Post;
import com.nebulohub.domain.user.ReadUserDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para enviar dados do post de volta ao cliente.
 * **ATUALIZADO** para incluir imageUrl E commentCount.
 */
public record ReadPostDto(
        Long id,
        String title,
        String description,
        String imageUrl,
        Double avgRating,
        Integer ratingCount,
        Integer commentCount, // <-- CHANGED from long
        LocalDateTime createdAt,
        ReadUserDto author,
        List<ReadCommentDto> recentComments // <-- REMOVED commentCount from here
) {
    /**
     * Construtor de conveniência (usado por findById).
     */
    public ReadPostDto(Post post) {
        this(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getImageUrl(),
                post.getAvgRating(),
                post.getRatingCount(),
                post.getCommentCount(), // <-- ADDED THIS
                post.getCreatedAt(),
                (post.getUser() != null) ? new ReadUserDto(post.getUser()) : null,
                List.of()
        );
    }

    /**
     * Construtor completo (usado por findAll).
     */
    // CHANGED: Removed 'long commentCount' from parameters
    public ReadPostDto(Post post, List<ReadCommentDto> recentComments) {
        this(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getImageUrl(),
                post.getAvgRating(),
                post.getRatingCount(),
                post.getCommentCount(), // <-- ADDED THIS
                post.getCreatedAt(),
                (post.getUser() != null) ? new ReadUserDto(post.getUser()) : null,
                recentComments
        );
    }
}