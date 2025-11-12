package com.nebulohub.service;

import com.nebulohub.domain.post.*;

import com.nebulohub.domain.rating.Rating;
import com.nebulohub.domain.rating.RatingRepository;
import com.nebulohub.domain.user.User;
import com.nebulohub.domain.user.UserRepository;
import com.nebulohub.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize; // <-- IMPORT ADDED
import org.springframework.security.core.Authentication; // <-- IMPORT ADDED
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List; // <-- IMPORT ADDED

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

    public Page<ReadPostDto> findAll(Pageable pageable) {
        return postRepository.findAllWithUserOrderByCreatedAtDesc(pageable)
                .map(ReadPostDto::new);
    }

    public ReadPostDto findById(Long id) {
        return postRepository.findById(id)
                .map(ReadPostDto::new)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));
    }

    @Transactional
    public ReadPostDto create(CreatePostDto dto, Authentication authentication) {
        // 1. Get the authenticated user from the token
        User author = (User) authentication.getPrincipal();

        // 2. Create the new post
        Post newPost = new Post();
        newPost.setTitle(dto.title());
        newPost.setDescription(dto.description());
        newPost.setUser(author); // Set the author

        // 3. Save and return DTO
        Post savedPost = postRepository.save(newPost);
        return new ReadPostDto(savedPost);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @postRepository.findById(#id).get().getUser().getId() == principal.id")
    public ReadPostDto update(Long id, UpdatePostDto dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));

        // Authorization is handled by @PreAuthorize

        if (dto.title() != null) {
            post.setTitle(dto.title());
        }
        if (dto.description() != null) {
            post.setDescription(dto.description());
        }

        Post updatedPost = postRepository.save(post);
        return new ReadPostDto(updatedPost);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @postRepository.findById(#id).get().getUser().getId() == principal.id")
    public void delete(Long id) {
        if (!postRepository.existsById(id)) {
            throw new NotFoundException("Post not found with id: " + id);
        }
        // Authorization is handled by @PreAuthorize

        postRepository.deleteById(id);
    }

    @Transactional
    public void updatePostRatingStats(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + postId));

        List<Rating> ratings = ratingRepository.findAllByPostId(postId);

        int ratingCount = ratings.size();
        double avgRating = ratings.stream()
                .mapToInt(Rating::getRatingValue)
                .average()
                .orElse(0.0);

        post.setRatingCount(ratingCount);
        post.setAvgRating(avgRating);

        postRepository.save(post);
    }
}