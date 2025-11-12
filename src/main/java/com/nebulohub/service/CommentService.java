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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /**
     * Creates a new comment.
     */
    @Transactional
    public ReadCommentDto create(CreateCommentDto dto) {
        // 1. Find the author and post
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + dto.userId()));
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

    /**
     * Updates an existing comment.
     */
    @Transactional
    public ReadCommentDto update(Long commentId, UpdateCommentDto dto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found with id: " + commentId));
        
        // TODO: Add authorization check here later.
        // e.g., if (!comment.getUser().getId().equals(authenticatedUserId)) {
        //    throw new BusinessException("You can only edit your own comments");
        // }

        comment.setContent(dto.content());
        Comment updatedComment = commentRepository.save(comment);
        return new ReadCommentDto(updatedComment);
    }

    /**
     * Deletes a comment by its ID.
     */
    @Transactional
    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found with id: " + commentId));
        
        // TODO: Add authorization check here later.
        // e.g., if (!comment.getUser().getId().equals(authenticatedUserId) && !postAuthorId.equals(authenticatedUserId)) {
        //    throw new BusinessException("You can only delete your own comments");
        // }

        commentRepository.delete(comment);
    }

    /**
     * Gets all comments for a specific post.
     */
    public Page<ReadCommentDto> getCommentsForPost(Long postId, Pageable pageable) {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException("Post not found with id: " + postId);
        }
        return commentRepository.findAllByPostIdWithUser(postId, pageable).map(ReadCommentDto::new);
    }

    /**
     * Gets all comments from a specific user.
     */
    public Page<ReadCommentDto> getCommentsFromUser(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        return commentRepository.findAllByUserIdWithPost(userId, pageable).map(ReadCommentDto::new);
    }
}