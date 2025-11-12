package com.nebulohub.domain.rating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;



public record SubmitRatingDto(
        @NotNull(message = "Rating value is required")
        @Min(value = 0, message = "Rating must be at least 0")
        @Max(value = 10, message = "Rating must be at most 10")
        Integer ratingValue,

        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Post ID is required")
        Long postId
) {
}