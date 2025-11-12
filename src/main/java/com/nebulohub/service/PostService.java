package com.nebulohub.service;

import com.nebulohub.domain.post.*;
import com.nebulohub.domain.user.User;
import com.nebulohub.domain.user.UserRepository;
import com.nebulohub.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository; // To link the post to a user

    /**
     * Finds all posts with pagination.
     */
    public Page<ReadPostDto> findAll(Pageable pageable) {
        // Use the custom query to fetch posts and users efficiently
        return postRepository.findAllWithUserOrderByCreatedAtDesc(pageable)
                .map(ReadPostDto::new);
    }

    /**
     * Finds a single post by its ID.
     */
    public ReadPostDto findById(Long id) {
        return postRepository.findById(id)
                .map(ReadPostDto::new)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));
    }

    /**
     * Creates a new post.
     */
    @Transactional
    public ReadPostDto create(CreatePostDto dto) {
        // 1. Find the author (User)
        // Later, we'll get this from the Spring Security context
        User author = userRepository.findById(dto.userId())
                .orElseThrow(() -> new NotFoundException("User (author) not found with id: " + dto.userId()));

        // 2. Create the new post
        Post newPost = new Post();
        newPost.setTitle(dto.title());
        newPost.setDescription(dto.description());
        newPost.setUser(author);
        
        // Default values (avgRating=0, ratingCount=0) are set in the entity

        // 3. Save and return DTO
        Post savedPost = postRepository.save(newPost);
        return new ReadPostDto(savedPost);
    }

    /**
     * Updates an existing post.
     */
    @Transactional
    public ReadPostDto update(Long id, UpdatePostDto dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));

        // We'll add an authorization check here later (e.g., is user the author?)

        // Perform partial updates
        if (dto.title() != null) {
            post.setTitle(dto.title());
        }
        if (dto.description() != null) {
            post.setDescription(dto.description());
        }

        Post updatedPost = postRepository.save(post);
        return new ReadPostDto(updatedPost);
    }

    /**
     * Deletes a post by its ID.
     * Cascade deletion for ratings and comments will be handled by JPA.
     */
    @Transactional
    public void delete(Long id) {
        if (!postRepository.existsById(id)) {
            throw new NotFoundException("Post not found with id: " + id);
        }
        // We'll add an authorization check here later
        
        postRepository.deleteById(id);
    }
}