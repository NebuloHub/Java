package com.nebulohub.service;

import com.nebulohub.domain.post.Post;
import com.nebulohub.domain.post.PostRepository;
import com.nebulohub.domain.rating.Rating;
import com.nebulohub.domain.rating.RatingRepository;

import com.nebulohub.domain.rating.ReadRatingDto;
import com.nebulohub.domain.rating.SubmitRatingDto;
import com.nebulohub.domain.user.User;
import com.nebulohub.domain.user.UserRepository;
import com.nebulohub.exception.BusinessException;
import com.nebulohub.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional; // <-- IMPORT ADDED

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostService postService;

    /**
     * **LOGIC UPDATED**
     * Creates a new rating or updates an existing one (upsert).
     * Also checks for self-rating.
     */
    @Transactional
    public ReadRatingDto createOrUpdate(SubmitRatingDto dto) { // <-- DTO RENAMED
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + dto.userId()));

        Post post = postRepository.findById(dto.postId())
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + dto.postId()));

        // 1. Check if user is rating their own post
        if (post.getUser().getId().equals(dto.userId())) {
            throw new BusinessException("You cannot rate your own post");
        }

        // 2. Check if user already rated this post
        Optional<Rating> existingRatingOpt = ratingRepository.findByUserIdAndPostId(dto.userId(), dto.postId());

        Rating ratingToSave;
        if (existingRatingOpt.isPresent()) {
            // **UPDATE logic**
            ratingToSave = existingRatingOpt.get();
            ratingToSave.setRatingValue(dto.ratingValue());
        } else {
            // **CREATE logic**
            ratingToSave = new Rating();
            ratingToSave.setRatingValue(dto.ratingValue());
            ratingToSave.setUser(user);
            ratingToSave.setPost(post);
        }

        // 3. Save the new or updated rating
        Rating savedRating = ratingRepository.save(ratingToSave);

        // 4. Update the post's average rating (delegating to PostService)
        postService.updatePostRatingStats(dto.postId());

        return new ReadRatingDto(savedRating);
    }

    /**
     * Deletes a rating by its ID.
     * Also recalculates the average rating for the post.
     */
    @Transactional
    public void delete(Long ratingId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new NotFoundException("Rating not found with id: " + ratingId));

        Long postId = rating.getPost().getId();

        // 1. Delete the rating
        ratingRepository.delete(rating);

        // 2. Update the post's average rating
        postService.updatePostRatingStats(postId);
    }

    /**
     * Gets all ratings for a specific post.
     */
    public Page<ReadRatingDto> getRatingsForPost(Long postId, Pageable pageable) {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException("Post not found with id: " + postId);
        }
        return ratingRepository.findAllByPostIdWithUser(postId, pageable).map(ReadRatingDto::new);
    }

    /**
     * Gets all ratings from a specific user.
     */
    public Page<ReadRatingDto> getRatingsFromUser(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        return ratingRepository.findAllByUserIdWithPost(userId, pageable).map(ReadRatingDto::new);
    }
}