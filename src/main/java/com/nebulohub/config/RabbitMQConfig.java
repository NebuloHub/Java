package com.nebulohub.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "ex.nebulohub.direct";

    // --- RATING
    public static final String QUEUE_POST_RATING_UPDATE = "q.post-rating-update";
    public static final String ROUTING_KEY_POST_RATING_UPDATE = "post-rating-update";

    // --- COMMENT
    public static final String QUEUE_POST_COMMENT_UPDATE = "q.post-comment-update";
    public static final String ROUTING_KEY_POST_COMMENT_UPDATE = "post-comment-update";


    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    // --- Rating Queue
    @Bean
    public Queue postRatingUpdateQueue() {
        return new Queue(QUEUE_POST_RATING_UPDATE);
    }

    @Bean
    public Binding postRatingUpdateBinding(Queue postRatingUpdateQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(postRatingUpdateQueue)
                .to(directExchange)
                .with(ROUTING_KEY_POST_RATING_UPDATE);
    }

    // --- Comment Queue
    @Bean
    public Queue postCommentUpdateQueue() {
        return new Queue(QUEUE_POST_COMMENT_UPDATE);
    }

    @Bean
    public Binding postCommentUpdateBinding(Queue postCommentUpdateQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(postCommentUpdateQueue)
                .to(directExchange)
                .with(ROUTING_KEY_POST_COMMENT_UPDATE);
    }

    // --- Shared Beans
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}