package com.nebulohub.domain.post.dto;

import com.nebulohub.domain.comment.ReadCommentDto;
import com.nebulohub.domain.post.Post;
import com.nebulohub.domain.user.ReadUserDto;


import java.time.LocalDateTime;
import java.util.List; // <-- IMPORT ADDED

/**
 * DTO for sending post data back to the client.
 * Includes nested author information.
 *
 * **UPDATED** to include commentCount and a list of recent comments.
 */
public record ReadPostDto(
        Long id,
        String title,
        String description,
        Double avgRating,
        Integer ratingCount,
        LocalDateTime createdAt,
        ReadUserDto author, // Nested DTO for author details

        // --- NEWLY ADDED FIELDS ---
        long commentCount,
        List<ReadCommentDto> recentComments
) {
    /**
     * Convenience constructor to map a Post entity to this DTO.
     * This constructor is now primarily used for the /posts/{id} details page.
     */
    public ReadPostDto(Post post) {
        this(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getAvgRating(),
                post.getRatingCount(),
                post.getCreatedAt(),
                (post.getUser() != null) ? new ReadUserDto(post.getUser()) : null,
                // We pass 0 and an empty list as defaults
                0,
                List.of()
        );
    }

    /**
     * **NEWLY ADDED CONSTRUCTOR**
     * This constructor is used by the PostService.findAll to build the
     * complete DTO with comment previews.
     */
    public ReadPostDto(Post post, long commentCount, List<ReadCommentDto> recentComments) {
        this(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getAvgRating(),
                post.getRatingCount(),
                post.getCreatedAt(),
                (post.getUser() != null) ? new ReadUserDto(post.getUser()) : null,
                commentCount,
                recentComments
        );
    }
}