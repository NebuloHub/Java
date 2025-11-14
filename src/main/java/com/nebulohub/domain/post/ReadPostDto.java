package com.nebulohub.domain.post;

import com.nebulohub.domain.comment.ReadCommentDto;
import com.nebulohub.domain.post.Post;
import com.nebulohub.domain.user.ReadUserDto;

import java.time.LocalDateTime;
import java.util.List;


public record ReadPostDto(
        Long id,
        String title,
        String description,
        String imageUrl,
        Double avgRating,
        Integer ratingCount,
        Integer commentCount,
        LocalDateTime createdAt,
        ReadUserDto author,
        List<ReadCommentDto> recentComments
) {

    public ReadPostDto(Post post) {
        this(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getImageUrl(),
                post.getAvgRating(),
                post.getRatingCount(),
                post.getCommentCount(),
                post.getCreatedAt(),
                (post.getUser() != null) ? new ReadUserDto(post.getUser()) : null,
                List.of()
        );
    }


    public ReadPostDto(Post post, List<ReadCommentDto> recentComments) {
        this(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getImageUrl(),
                post.getAvgRating(),
                post.getRatingCount(),
                post.getCommentCount(),
                post.getCreatedAt(),
                (post.getUser() != null) ? new ReadUserDto(post.getUser()) : null,
                recentComments
        );
    }
}