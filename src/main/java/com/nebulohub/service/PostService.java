package com.nebulohub.service;

import com.nebulohub.domain.comment.CommentRepository;
import com.nebulohub.domain.comment.ReadCommentDto;
import com.nebulohub.domain.post.*;

import com.nebulohub.domain.rating.Rating;
import com.nebulohub.domain.rating.RatingRepository;
import com.nebulohub.domain.user.User;
import com.nebulohub.domain.user.UserRepository;
import com.nebulohub.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final CommentRepository commentRepository;

    @Cacheable(cacheNames = "posts")
    public Page<ReadPostDto> findAll(Pageable pageable) {
        Page<Post> postPage = postRepository.findAllWithUserOrderByCreatedAtDesc(pageable);

        return postPage.map(post -> {
            List<ReadCommentDto> recentComments = commentRepository
                    .findTop3ByPostIdOrderByCreatedAtDesc(post.getId())
                    .stream()
                    .map(ReadCommentDto::new)
                    .collect(Collectors.toList());



            return new ReadPostDto(post, recentComments);
        });
    }

    @Cacheable(cacheNames = "userPosts", key = "{#userId, #pageable.pageNumber, #pageable.pageSize}")
    public Page<ReadPostDto> findAllByUserId(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        Page<Post> postPage = postRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);

        return postPage.map(post -> {
            List<ReadCommentDto> recentComments = commentRepository
                    .findTop3ByPostIdOrderByCreatedAtDesc(post.getId())
                    .stream()
                    .map(ReadCommentDto::new)
                    .collect(Collectors.toList());


            return new ReadPostDto(post, recentComments);
        });
    }

    public ReadPostDto findById(Long id) {
        return postRepository.findById(id)
                .map(ReadPostDto::new)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));
    }


    @Transactional
    @CacheEvict(cacheNames = {"posts", "userPosts"}, allEntries = true)
    public ReadPostDto create(CreatePostDto dto, Authentication authentication) {
        User author = (User) authentication.getPrincipal();

        Post newPost = new Post();
        newPost.setTitle(dto.title());
        newPost.setDescription(dto.description());
        newPost.setImageUrl(dto.imageUrl());
        newPost.setUser(author);

        Post savedPost = postRepository.save(newPost);
        return new ReadPostDto(savedPost);
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @postRepository.findById(#id).get().getUser().getId() == principal.id")
    @CacheEvict(cacheNames = {"posts", "userPosts"}, allEntries = true)
    public ReadPostDto update(Long id, UpdatePostDto dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));

        if (dto.title() != null) {
            post.setTitle(dto.title());
        }
        if (dto.description() != null) {
            post.setDescription(dto.description());
        }
        if (dto.imageUrl() != null) {
            post.setImageUrl(dto.imageUrl());
        }

        Post updatedPost = postRepository.save(post);
        return new ReadPostDto(updatedPost);
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @postRepository.findById(#id).get().getUser().getId() == principal.id")
    @CacheEvict(cacheNames = {"posts", "userPosts"}, allEntries = true)
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

    @Transactional
    public void updatePostCommentStats(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + postId));

        long commentCount = commentRepository.countByPostId(postId);

        post.setCommentCount((int) commentCount);
        postRepository.save(post);
    }
}