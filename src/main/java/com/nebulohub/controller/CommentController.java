package com.nebulohub.controller;


import com.nebulohub.domain.comment.CreateCommentDto;
import com.nebulohub.domain.comment.ReadCommentDto;
import com.nebulohub.domain.comment.UpdateCommentDto;
import com.nebulohub.service.CommentService;
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
@Tag(name = "Comments", description = "Post, edit, delete, and view comments")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Post a new comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment posted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "User or Post not found")
    })
    @PostMapping("/comments")
    public ResponseEntity<ReadCommentDto> create(
            @RequestBody @Valid CreateCommentDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        ReadCommentDto newComment = commentService.create(dto);
        URI uri = uriBuilder.path("/api/comments/{id}").buildAndExpand(newComment.id()).toUri();
        return ResponseEntity.created(uri).body(newComment);
    }

    @Operation(summary = "Update an existing comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @PutMapping("/comments/{id}")
    public ResponseEntity<ReadCommentDto> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateCommentDto dto
    ) {
        ReadCommentDto updatedComment = commentService.update(id, dto);
        return ResponseEntity.ok(updatedComment);
    }

    @Operation(summary = "Delete a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment deleted"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List all comments for a specific post (paginated, oldest first)")
    @ApiResponse(responseCode = "200", description = "Page of comments returned")
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<Page<ReadCommentDto>> getCommentsForPost(
            @PathVariable Long postId,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<ReadCommentDto> comments = commentService.getCommentsForPost(postId, pageable);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "List all comments from a specific user (paginated, newest first)")
    @ApiResponse(responseCode = "200", description = "Page of comments returned")
    @GetMapping("/users/{userId}/comments")
    public ResponseEntity<Page<ReadCommentDto>> getCommentsFromUser(
            @PathVariable Long userId,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<ReadCommentDto> comments = commentService.getCommentsFromUser(userId, pageable);
        return ResponseEntity.ok(comments);
    }
}