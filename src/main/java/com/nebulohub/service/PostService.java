package com.nebulohub.service;

import com.nebulohub.domain.post.*;
import com.nebulohub.domain.rating.Rating;
import com.nebulohub.domain.rating.RatingRepository; // <-- IMPORT ADDED
import com.nebulohub.domain.user.User;
import com.nebulohub.domain.user.UserRepository;
import com.nebulohub.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List; // <-- IMPORT ADDED

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository; // <-- REPOSITORY INJECTED

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
    public ReadPostDto create(CreatePostDto dto) {
        User author = userRepository.findById(dto.userId())
                .orElseThrow(() -> new NotFoundException("User (author) not found with id: " + dto.userId()));

        Post newPost = new Post();
        newPost.setTitle(dto.title());
        newPost.setDescription(dto.description());
        newPost.setUser(author);

        Post savedPost = postRepository.save(newPost);
        return new ReadPostDto(savedPost);
    }

    @Transactional
    public ReadPostDto update(Long id, UpdatePostDto dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));

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
    public void delete(Long id) {
        if (!postRepository.existsById(id)) {
            throw new NotFoundException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }

    /**
     * **NEWLY ADDED METHOD**
     * Recalculates and updates the average rating and rating count for a post.
     * This is called by the RatingService whenever a rating is created or deleted.
     */
    @Transactional
    public void updatePostRatingStats(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + postId));

        // Find all ratings for the post
        List<Rating> ratings = ratingRepository.findAllByPostId(postId);

        // Calculate new stats
        int ratingCount = ratings.size();
        double avgRating = ratings.stream()
                .mapToInt(Rating::getRatingValue)
                .average()
                .orElse(0.0);

        // Set and save the post
        post.setRatingCount(ratingCount);
        post.setAvgRating(avgRating); // The DB stores NUMBER(3,1) so it will be rounded

        postRepository.save(post);
    }
}