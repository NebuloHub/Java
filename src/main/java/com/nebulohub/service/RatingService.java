package com.nebulohub.service;

// --- Imports ---
import com.nebulohub.config.RabbitMQConfig; // <-- IMPORT ADDED
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
import com.nebulohub.service.message.PostRatingUpdateMessage; // <-- IMPORT ADDED
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate; // <-- IMPORT ADDED
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    // private final PostService postService; // We no longer call this directly

    // --- NEWLY INJECTED ---
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQConfig rabbitMQConfig; // Not strictly needed, but good for constants
    private final PostService postService; // <-- RE-ADD or KEEP this. Listener needs it. My mistake. It's the *call* we're removing.

    /**
     * **EVICT CACHE (ATUALIZADO)**
     * Limpa os caches "posts" e "userPosts".
     */
    @Transactional
    @CacheEvict(cacheNames = {"posts", "userPosts"}, allEntries = true)
    public ReadRatingDto createOrUpdate(SubmitRatingDto dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Post post = postRepository.findById(dto.postId())
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + dto.postId()));

        if (post.getUser().getId().equals(user.getId())) {
            throw new BusinessException("You cannot rate your own post");
        }

        Optional<Rating> existingRatingOpt = ratingRepository.findByUserIdAndPostId(user.getId(), dto.postId());

        Rating ratingToSave;
        if (existingRatingOpt.isPresent()) {
            ratingToSave = existingRatingOpt.get();
            ratingToSave.setRatingValue(dto.ratingValue());
        } else {
            ratingToSave = new Rating();
            ratingToSave.setRatingValue(dto.ratingValue());
            ratingToSave.setUser(user);
            ratingToSave.setPost(post);
        }

        Rating savedRating = ratingRepository.save(ratingToSave);

        // --- OLD SYNCHRONOUS CALL (REMOVED) ---
        // postService.updatePostRatingStats(dto.postId());

        // --- NEW ASYNCHRONOUS CALL (ADDED) ---
        publishRatingUpdate(dto.postId());

        return new ReadRatingDto(savedRating);
    }

    /**
     * **EVICT CACHE (ATUALIZADO)**
     * Limpa os caches "posts" e "userPosts".
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @ratingRepository.findById(#ratingId).get().getUser().getId() == principal.id")
    @CacheEvict(cacheNames = {"posts", "userPosts"}, allEntries = true)
    public void delete(Long ratingId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new NotFoundException("Rating not found with id: " + ratingId));

        Long postId = rating.getPost().getId();
        ratingRepository.delete(rating);

        // --- OLD SYNCHRONOUS CALL (REMOVED) ---
        // postService.updatePostRatingStats(postId);

        // --- NEW ASYNCHRONOUS CALL (ADDED) ---
        publishRatingUpdate(postId);
    }

    /**
     * **NEW PRIVATE HELPER METHOD**
     * Publishes a message to RabbitMQ to update the stats for a post.
     */
    private void publishRatingUpdate(Long postId) {
        PostRatingUpdateMessage message = new PostRatingUpdateMessage(postId);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY_POST_RATING_UPDATE,
                message
        );
    }


    // --- NO CHANGES to the methods below ---

    public Page<ReadRatingDto> getRatingsForPost(Long postId, Pageable pageable) {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException("Post not found with id: " + postId);
        }
        return ratingRepository.findAllByPostIdWithUser(postId, pageable).map(ReadRatingDto::new);
    }

    public Page<ReadRatingDto> getRatingsFromUser(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        return ratingRepository.findAllByUserIdWithPost(userId, pageable).map(ReadRatingDto::new);
    }
}