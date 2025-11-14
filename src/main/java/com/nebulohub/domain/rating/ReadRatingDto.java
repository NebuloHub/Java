package com.nebulohub.domain.rating;

import com.nebulohub.domain.rating.Rating;


public record ReadRatingDto(
        Long id,
        Integer ratingValue,
        Long userId,
        String username,
        Long postId,
        String postTitle
) {

    public ReadRatingDto(Rating rating) {
        this(
                rating.getId(),
                rating.getRatingValue(),
                rating.getUser() != null ? rating.getUser().getId() : null,
                rating.getUser() != null ? rating.getUser().getActualUsername() : null,
                rating.getPost() != null ? rating.getPost().getId() : null,
                rating.getPost() != null ? rating.getPost().getTitle() : null
        );
    }
}