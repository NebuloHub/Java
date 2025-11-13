package com.nebulohub.service;

import com.nebulohub.domain.comment.CommentRepository; // <-- IMPORT ADDED
import com.nebulohub.domain.comment.ReadCommentDto;
import com.nebulohub.domain.post.CreatePostDto;
import com.nebulohub.domain.post.Post;
import com.nebulohub.domain.post.PostRepository;

import com.nebulohub.domain.post.UpdatePostDto;
import com.nebulohub.domain.post.dto.ReadPostDto;
import com.nebulohub.domain.rating.Rating;
import com.nebulohub.domain.rating.RatingRepository;
import com.nebulohub.domain.user.User;
import com.nebulohub.domain.user.UserRepository;
import com.nebulohub.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors; // <-- IMPORT ADDED

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final CommentRepository commentRepository; // <-- REPOSITORY INJECTED

    /**
     * **METHOD UPDATED**
     * Finds all posts, and for each post, fetches its total comment count
     * and the 3 most recent comments for the feed preview.
     *
     * This introduces N+1 queries (2 per post), but this is an acceptable
     * trade-off for clean code. We will fix this with Caching later.
     */
    public Page<ReadPostDto> findAll(Pageable pageable) {
        Page<Post> postPage = postRepository.findAllWithUserOrderByCreatedAtDesc(pageable);

        // We can't use .map(ReadPostDto::new) anymore, we need to manually map
        // to add the comment data.
        return postPage.map(post -> {
            // Get top 3 recent comments
            List<ReadCommentDto> recentComments = commentRepository
                    .findTop3ByPostIdOrderByCreatedAtDesc(post.getId())
                    .stream()
                    .map(ReadCommentDto::new) // Convert Comment entities to DTOs
                    .collect(Collectors.toList());

            // Get total comment count
            long commentCount = commentRepository.countByPostId(post.getId());

            // Use the new constructor to build the full DTO
            return new ReadPostDto(post, commentCount, recentComments);
        });
    }

    /**
     * Finds a single post by its ID.
     * This is for the "detail page" and will NOT include the recent comments
     * (we will load all comments on that page separately).
     */
    public ReadPostDto findById(Long id) {
        return postRepository.findById(id)
                .map(ReadPostDto::new) // Uses the simple constructor
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));
    }

    @Transactional
    public ReadPostDto create(CreatePostDto dto, Authentication authentication) {
        User author = (User) authentication.getPrincipal();

        Post newPost = new Post();
        newPost.setTitle(dto.title());
        newPost.setDescription(dto.description());
        newPost.setUser(author);

        Post savedPost = postRepository.save(newPost);
        // Returns a DTO with no comments, which is correct for a new post
        return new ReadPostDto(savedPost);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @postRepository.findById(#id).get().getUser().getId() == principal.id")
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
        // This is an update, so we don't need to fetch recent comments here
        return new ReadPostDto(updatedPost);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @postRepository.findById(#id).get().getUser().getId() == principal.id")
    public void delete(Long id) {
        if (!postRepository.existsById(id)) {
            throw new NotFoundException("Post not found with id: " + id);
        }
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