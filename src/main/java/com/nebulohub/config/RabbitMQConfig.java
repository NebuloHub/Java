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
    public static final String QUEUE_POST_RATING_UPDATE = "q.post-rating-update";
    public static final String ROUTING_KEY_POST_RATING_UPDATE = "post-rating-update";

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

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

    @Bean
    public MessageConverter jsonMessageConverter() {
        // This ensures our message DTOs are serialized to JSON
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        // This configures the default RabbitTemplate to use JSON
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}