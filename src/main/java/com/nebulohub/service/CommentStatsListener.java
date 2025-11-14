package com.nebulohub.service;

import com.nebulohub.config.RabbitMQConfig;
import com.nebulohub.service.message.PostCommentUpdateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentStatsListener {

    private final PostService postService;


    @RabbitListener(queues = RabbitMQConfig.QUEUE_POST_COMMENT_UPDATE)
    public void handleCommentUpdate(PostCommentUpdateMessage message) {
        if (message == null || message.postId() == null) {
            log.warn("Received null or invalid message in CommentStatsListener. Discarding.");
            return;
        }

        try {
            log.info("Received message to update comment stats for post ID: {}", message.postId());
            postService.updatePostCommentStats(message.postId());
            log.info("Successfully updated comment stats for post ID: {}", message.postId());
        } catch (Exception e) {
            log.error("Failed to update post comment stats for post ID: {}. Error: {}",
                    message.postId(), e.getMessage(), e);
        }
    }
}