package com.nebulohub.controller;


import com.nebulohub.domain.post.CreatePostDto;
import com.nebulohub.domain.post.ReadPostDto;
import com.nebulohub.domain.post.UpdatePostDto;
import com.nebulohub.service.PostService;
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
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Create, read, and manage startup idea posts")
public class PostController {

    private final PostService postService;

    @Operation(summary = "Create a new post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Author (user) not found")
    })
    @PostMapping
    public ResponseEntity<ReadPostDto> create(
            @RequestBody @Valid CreatePostDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        ReadPostDto newPost = postService.create(dto);
        URI uri = uriBuilder.path("/api/posts/{id}").buildAndExpand(newPost.id()).toUri();
        return ResponseEntity.created(uri).body(newPost);
    }

    @Operation(summary = "List all posts (paginated, newest first)")
    @ApiResponse(responseCode = "200", description = "Page of posts returned")
    @GetMapping
    public ResponseEntity<Page<ReadPostDto>> listAll(
            @ParameterObject
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<ReadPostDto> posts = postService.findAll(pageable);
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "Get a single post by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post found"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReadPostDto> getById(@PathVariable Long id) {
        ReadPostDto post = postService.findById(id);
        return ResponseEntity.ok(post);
    }

    @Operation(summary = "Update a post's title or description")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReadPostDto> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdatePostDto dto
    ) {
        ReadPostDto updatedPost = postService.update(id, dto);
        return ResponseEntity.ok(updatedPost);
    }

    @Operation(summary = "Delete a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
}