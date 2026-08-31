package net.patrykdobrowolski.bookshelf.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

    @Bean
    public TopicExchange commandExchange(@Value("${rabbitmq.command-exchange}") String commandExchangeName) {
        return new TopicExchange(commandExchangeName);
    }

    @Bean
    public Queue fetchBookCommandQueue(@Value("${rabbitmq.fetch-book-command-queue}") String fetchBookCommandQueueName) {
        return QueueBuilder.durable(fetchBookCommandQueueName).build();
    }

    @Bean
    public Queue retryfetchBookCommandQueue(
            @Value("${rabbitmq.command-exchange}") String commandExchangeName,
            @Value("${rabbitmq.fetch-book-command-retry-queue}") String fetchBookCommandRetryQueueName,
            @Value("${rabbitmq.fetch-book-command-queue}") String fetchBookCommandQueueName) {

        return QueueBuilder.durable(fetchBookCommandRetryQueueName)
                .withArgument("x-message-ttl", 30000)
                .withArgument("x-dead-letter-exchange", commandExchangeName)
                .withArgument("x-dead-letter-routing-key", fetchBookCommandQueueName)
                .build();
    }

    @Bean
    public Binding fetchBookCommandQueueBinding(
            Queue fetchBookCommandQueue, TopicExchange commandExchange,
            @Value("${rabbitmq.fetch-book-command-queue}") String fetchBookCommandQueueName) {
        return BindingBuilder.bind(fetchBookCommandQueue).to(commandExchange).with(fetchBookCommandQueueName);
    }

    @Bean
    public Binding fetchBookRetryQueueBinding(
            Queue retryfetchBookCommandQueue, TopicExchange commandExchange,
            @Value("${rabbitmq.fetch-book-command-retry-queue}") String fetchBookCommandRetryQueueName) {
        return BindingBuilder.bind(retryfetchBookCommandQueue).to(commandExchange).with(fetchBookCommandRetryQueueName);
    }

    @Bean
    public Queue exportSessionCommandQueue(@Value("${rabbitmq.export-session-command-queue}") String exportSessionCommandQueue) {
        return QueueBuilder.durable(exportSessionCommandQueue).build();
    }

    @Bean
    public Binding exportSessionCommandQueueBinding(
            Queue exportSessionCommandQueue, TopicExchange commandExchange,
            @Value("${rabbitmq.export-session-command-queue}") String exportSessionCommandQueueName) {
        return BindingBuilder.bind(exportSessionCommandQueue).to(commandExchange).with(exportSessionCommandQueueName);
    }

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter("net.patrykdobrowolski.bookshelf.adapter.rabbitmq.dto");
    }
}
