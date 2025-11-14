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
import org.springframework.cache.annotation.CacheEvict; // <-- IMPORT ADICIONADO
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
    private final PostService postService;

    /**
     * **EVICT CACHE**
     * Limpa o cache "posts" porque uma nova avaliação
     * muda o avgRating e o ratingCount.
     */
    @Transactional
    @CacheEvict(cacheNames = "posts", allEntries = true)
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
        postService.updatePostRatingStats(dto.postId());
        return new ReadRatingDto(savedRating);
    }

    /**
     * **EVICT CACHE**
     * Limpa o cache "posts" porque uma avaliação deletada
     * muda o avgRating e o ratingCount.
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @ratingRepository.findById(#ratingId).get().getUser().getId() == principal.id")
    @CacheEvict(cacheNames = "posts", allEntries = true)
    public void delete(Long ratingId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new NotFoundException("Rating not found with id: " + ratingId));

        Long postId = rating.getPost().getId();
        ratingRepository.delete(rating);
        postService.updatePostRatingStats(postId);
    }

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