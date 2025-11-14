package com.nebulohub.service;

import com.nebulohub.config.RabbitMQConfig; // <-- IMPORT ADDED
import com.nebulohub.domain.comment.*;
import com.nebulohub.domain.post.Post;
import com.nebulohub.domain.post.PostRepository;
import com.nebulohub.domain.user.User;
import com.nebulohub.domain.user.UserRepository;
import com.nebulohub.exception.NotFoundException;
import com.nebulohub.service.message.PostCommentUpdateMessage; // <-- IMPORT ADDED
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate; // <-- IMPORT ADDED
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    @CacheEvict(cacheNames = {"posts", "userPosts", "userRecentComments"}, allEntries = true)
    public ReadCommentDto create(CreateCommentDto dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Post post = postRepository.findById(dto.postId())
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + dto.postId()));

        Comment newComment = new Comment();
        newComment.setContent(dto.content());
        newComment.setUser(user);
        newComment.setPost(post);

        Comment savedComment = commentRepository.save(newComment);

        publishCommentUpdate(dto.postId());

        return new ReadCommentDto(savedComment);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @commentRepository.findById(#commentId).get().getUser().getId() == principal.id")
    @CacheEvict(cacheNames = {"posts", "userPosts", "userRecentComments"}, allEntries = true)
    public ReadCommentDto update(Long commentId, UpdateCommentDto dto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found with id: " + commentId));

        comment.setContent(dto.content());
        Comment updatedComment = commentRepository.save(comment);

        return new ReadCommentDto(updatedComment);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @commentRepository.findById(#commentId).get().getUser().getId() == principal.id")
    @CacheEvict(cacheNames = {"posts", "userPosts", "userRecentComments"}, allEntries = true)
    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found with id: " + commentId));

        Long postId = comment.getPost().getId();

        commentRepository.deleteById(commentId);


        publishCommentUpdate(postId);
    }


    private void publishCommentUpdate(Long postId) {
        PostCommentUpdateMessage message = new PostCommentUpdateMessage(postId);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY_POST_COMMENT_UPDATE,
                message
        );
    }


    public Page<ReadCommentDto> getCommentsForPost(Long postId, Pageable pageable) {

        if (!postRepository.existsById(postId)) {
            throw new NotFoundException("Post not found with id: " + postId);
        }
        return commentRepository.findAllByPostIdWithUser(postId, pageable).map(ReadCommentDto::new);
    }

    @Cacheable(cacheNames = "userRecentComments", key = "#userId")
    public Page<ReadCommentDto> getCommentsFromUser(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        return commentRepository.findAllByUserIdWithPost(userId, pageable).map(ReadCommentDto::new);
    }
}