package com.nebulohub.controller;


import com.nebulohub.domain.rating.SubmitRatingDto;
import com.nebulohub.domain.rating.ReadRatingDto;
import com.nebulohub.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Ratings", description = "Submit, delete, and view ratings for posts")
public class RatingController {

    private final RatingService ratingService;

    @Operation(summary = "Submit or Update a rating for a post (Upsert)") // <-- UPDATED
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rating submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data (e.g., value > 10, or self-rating)"),
            @ApiResponse(responseCode = "404", description = "User or Post not found")
            // 409 Conflict is removed, as we now handle updates
    })
    @PostMapping("/ratings")
    public ResponseEntity<ReadRatingDto> createOrUpdate( // <-- UPDATED
                                                         @RequestBody @Valid SubmitRatingDto dto, // <-- DTO RENAMED
                                                         UriComponentsBuilder uriBuilder
    ) {
        // Use the new service method
        ReadRatingDto newRating = ratingService.createOrUpdate(dto);
        URI uri = uriBuilder.path("/api/ratings/{id}").buildAndExpand(newRating.id()).toUri();

        // Return 201 Created, which is fine for an upsert
        return ResponseEntity.created(uri).body(newRating);
    }

    @Operation(summary = "Delete (retract) a rating")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rating deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Rating not found")
    })
    @DeleteMapping("/ratings/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ratingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List all ratings for a specific post")
    @ApiResponse(responseCode = "200", description = "Page of ratings returned")
    @GetMapping("/posts/{postId}/ratings")
    public ResponseEntity<Page<ReadRatingDto>> getRatingsForPost(
            @PathVariable Long postId,
            @ParameterObject @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        Page<ReadRatingDto> ratings = ratingService.getRatingsForPost(postId, pageable);
        return ResponseEntity.ok(ratings);
    }

    @Operation(summary = "List all ratings from a specific user")
    @ApiResponse(responseCode = "200", description = "Page of ratings returned")
    @GetMapping("/users/{userId}/ratings")
    public ResponseEntity<Page<ReadRatingDto>> getRatingsFromUser(
            @PathVariable Long userId,
            @ParameterObject @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        Page<ReadRatingDto> ratings = ratingService.getRatingsFromUser(userId, pageable);
        return ResponseEntity.ok(ratings);
    }
}