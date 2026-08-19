package net.patrykdobrowolski.bookscanner.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

    public static final String QUEUE_NAME = "bookscanner.queue.command.fetchBookDetails";
    public static final String DLQ_NAME = QUEUE_NAME + ".dlq";
    public static final String EXCHANGE_NAME = "bookscanner.main";
    public static final String DLQ_EXCHANGE_NAME = "global.dlx";
    public static final String ROUTING_KEY = "bookscanner.command.fetchBookDetails";
    public static final String DLQ_ROUTING_KEY = "dlq.bookscanner.command.fetchBookDetails";

    @Bean
    public Exchange dlqExchange() {
        return new TopicExchange(DLQ_EXCHANGE_NAME);
    }

    @Bean
    public Queue fetchBookDetailsQueue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", DLQ_EXCHANGE_NAME)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue fetchBookDetailsDLQueue() {
        return new Queue(DLQ_NAME, true);
    }

    @Bean
    public Exchange bookScannerExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue fetchBookDetailsQueue, Exchange bookScannerExchange) {
        return BindingBuilder.bind(fetchBookDetailsQueue).to(bookScannerExchange).with(ROUTING_KEY).noargs();
    }

    @Bean
    public Binding dlBinding(Queue fetchBookDetailsDLQueue, Exchange dlqExchange) {
        return BindingBuilder.bind(fetchBookDetailsDLQueue).to(dlqExchange).with(DLQ_ROUTING_KEY).noargs();
    }

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter("net.patrykdobrowolski.bookscanner.rabbitmq.dto");
    }
}
