package com.nebulohub.service;

import com.nebulohub.config.RabbitMQConfig;
import com.nebulohub.service.message.PostRatingUpdateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingStatsListener {

    private final PostService postService;

    /**
     * Listens to the post-rating-update queue and recalculates stats.
     * This now happens asynchronously, not blocking the user's request.
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_POST_RATING_UPDATE)
    public void handleRatingUpdate(PostRatingUpdateMessage message) {
        if (message == null || message.postId() == null) {
            log.warn("Received null or invalid message in RatingStatsListener. Discarding.");
            return;
        }

        try {
            log.info("Received message to update stats for post ID: {}", message.postId());
            // This is the work we wanted to make asynchronous
            postService.updatePostRatingStats(message.postId());
            log.info("Successfully updated stats for post ID: {}", message.postId());
        } catch (Exception e) {
            // Log the error, but don't re-throw.
            // This prevents the message from being re-queued indefinitely (a "poison pill").
            log.error("Failed to update post stats for post ID: {}. Error: {}",
                    message.postId(), e.getMessage(), e);
        }
    }
}