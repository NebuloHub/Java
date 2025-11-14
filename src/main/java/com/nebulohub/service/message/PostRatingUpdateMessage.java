package com.nebulohub.service.message;

import java.io.Serializable;

/**
 * Message DTO sent to RabbitMQ when a post's rating is changed.
 * Implements Serializable, which is good practice for DTOs sent over the wire.
 */
public record PostRatingUpdateMessage(
        Long postId
) implements Serializable {
}