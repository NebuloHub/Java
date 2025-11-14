package com.nebulohub.service.message;

import java.io.Serializable;

/**
 * Message DTO sent to RabbitMQ when a post's comment count is changed.
 */
public record PostCommentUpdateMessage(
        Long postId
) implements Serializable {
}