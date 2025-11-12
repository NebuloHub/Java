package com.nebulohub.service;

import com.nebulohub.domain.comment.*;
import com.nebulohub.domain.post.Post;
import com.nebulohub.domain.post.PostRepository;
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

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public ReadCommentDto create(CreateCommentDto dto, Authentication authentication) {
        // 1. Get the authenticated user from the token
        User user = (User) authentication.getPrincipal();

        Post post = postRepository.findById(dto.postId())
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + dto.postId()));

        // 2. Create and save new comment
        Comment newComment = new Comment();
        newComment.setContent(dto.content());
        newComment.setUser(user);
        newComment.setPost(post);

        Comment savedComment = commentRepository.save(newComment);
        return new ReadCommentDto(savedComment);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @commentRepository.findById(#commentId).get().getUser().getId() == principal.id")
    public ReadCommentDto update(Long commentId, UpdateCommentDto dto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found with id: " + commentId));

        // Authorization is handled by @PreAuthorize

        comment.setContent(dto.content());
        Comment updatedComment = commentRepository.save(comment);
        return new ReadCommentDto(updatedComment);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @commentRepository.findById(#commentId).get().getUser().getId() == principal.id")
    public void delete(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException("Comment not found with id: " + commentId);
        }

        // Authorization is handled by @PreAuthorize

        commentRepository.deleteById(commentId);
    }

    public Page<ReadCommentDto> getCommentsForPost(Long postId, Pageable pageable) {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException("Post not found with id: " + postId);
        }
        return commentRepository.findAllByPostIdWithUser(postId, pageable).map(ReadCommentDto::new);
    }

    public Page<ReadCommentDto> getCommentsFromUser(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        return commentRepository.findAllByUserIdWithPost(userId, pageable).map(ReadCommentDto::new);
    }
}