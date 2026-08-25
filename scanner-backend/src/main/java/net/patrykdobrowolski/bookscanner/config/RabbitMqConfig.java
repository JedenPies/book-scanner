package net.patrykdobrowolski.bookscanner.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

    @Bean
    public DirectExchange fetchBookCommandExchange(@Value("${rabbitmq.fetch-book-command-exchange}") String fetchBookCommandExchangeName) {
        return new DirectExchange(fetchBookCommandExchangeName);
    }

    @Bean
    public Queue fetchBookCommandQueue(@Value("${rabbitmq.fetch-book-command-queue}") String fetchBookCommandQueueName) {
        return QueueBuilder.durable(fetchBookCommandQueueName).build();
    }

    @Bean
    public Queue retryfetchBookCommandQueue(
            @Value("${rabbitmq.fetch-book-command-exchange}") String fetchBookCommandExchangeName,
            @Value("${rabbitmq.fetch-book-command-retry-queue}") String fetchBookCommandRetryQueueName,
            @Value("${rabbitmq.fetch-book-command-queue}") String fetchBookCommandQueueName) {

        return QueueBuilder.durable(fetchBookCommandRetryQueueName)
                .withArgument("x-message-ttl", 30000)
                .withArgument("x-dead-letter-exchange", fetchBookCommandExchangeName)
                .withArgument("x-dead-letter-routing-key", fetchBookCommandQueueName)
                .build();
    }

    @Bean
    public Binding fetchBookCommandQueueBinding(
            Queue fetchBookCommandQueue, DirectExchange fetchBookCommandExchange,
            @Value("${rabbitmq.fetch-book-command-queue}") String fetchBookCommandQueueName) {
        return BindingBuilder.bind(fetchBookCommandQueue).to(fetchBookCommandExchange).with(fetchBookCommandQueueName);
    }

    @Bean
    public Binding retryQueueBinding(
            Queue retryfetchBookCommandQueue, DirectExchange fetchBookCommandExchange,
            @Value("${rabbitmq.fetch-book-command-retry-queue}") String fetchBookCommandRetryQueueName) {
        return BindingBuilder.bind(retryfetchBookCommandQueue).to(fetchBookCommandExchange).with(fetchBookCommandRetryQueueName);
    }

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter("net.patrykdobrowolski.bookscanner.rabbitmq.dto");
    }
}
