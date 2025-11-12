package com.nebulohub.domain.post;

import com.nebulohub.domain.post.Post;
import com.nebulohub.domain.user.ReadUserDto;

import java.time.LocalDateTime;

/**
 * DTO for sending post data back to the client.
 * Includes nested author (user) information.
 */
public record ReadPostDto(
        Long id,
        String title,
        String description,
        Double avgRating,
        Integer ratingCount,
        LocalDateTime createdAt,
        ReadUserDto author
) {

    public ReadPostDto(Post post) {
        this(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getAvgRating(),
                post.getRatingCount(),
                post.getCreatedAt(),

                (post.getUser() != null) ? new ReadUserDto(post.getUser()) : null
        );
    }
}