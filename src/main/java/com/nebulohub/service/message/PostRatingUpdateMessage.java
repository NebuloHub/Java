package com.nebulohub.service.message;

import java.io.Serializable;

/**
 * Message DTO sent to RabbitMQ when a post's rating is changed.
 */
public record PostRatingUpdateMessage(
        Long postId
) implements Serializable {
}