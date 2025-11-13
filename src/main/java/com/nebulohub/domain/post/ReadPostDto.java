package com.nebulohub.domain.post.dto;

import com.nebulohub.domain.comment.ReadCommentDto;
import com.nebulohub.domain.post.Post;
import com.nebulohub.domain.user.ReadUserDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para enviar dados do post de volta ao cliente.
 * **ATUALIZADO** para incluir imageUrl.
 */
public record ReadPostDto(
        Long id,
        String title,
        String description,
        String imageUrl, // <-- CAMPO ADICIONADO
        Double avgRating,
        Integer ratingCount,
        LocalDateTime createdAt,
        ReadUserDto author,

        long commentCount,
        List<ReadCommentDto> recentComments
) {
    /**
     * Construtor de conveniência (usado por findById).
     */
    public ReadPostDto(Post post) {
        this(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getImageUrl(), // <-- CAMPO ADICIONADO
                post.getAvgRating(),
                post.getRatingCount(),
                post.getCreatedAt(),
                (post.getUser() != null) ? new ReadUserDto(post.getUser()) : null,
                0,
                List.of()
        );
    }

    /**
     * Construtor completo (usado por findAll).
     */
    public ReadPostDto(Post post, long commentCount, List<ReadCommentDto> recentComments) {
        this(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getImageUrl(), // <-- CAMPO ADICIONADO
                post.getAvgRating(),
                post.getRatingCount(),
                post.getCreatedAt(),
                (post.getUser() != null) ? new ReadUserDto(post.getUser()) : null,
                commentCount,
                recentComments
        );
    }
}