package net.patrykdobrowolski.bookscanner.rabbitmq;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.domain.model.Session;
import net.patrykdobrowolski.bookscanner.domain.port.BookDetailsAsyncFetcherPort;
import net.patrykdobrowolski.bookscanner.rabbitmq.dto.FetchBookDetailsCommandDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

@Named
@RequiredArgsConstructor
@Slf4j
public class BookDetailsAsyncFetcherAdapter implements BookDetailsAsyncFetcherPort {

    @Value("${rabbitmq.fetch-book-command-exchange}")
    private String fetchBookCommandExchangeName;

    @Value("${rabbitmq.fetch-book-command-queue}")
    private String fetchBookCommandRetryQueueName;

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void fetchBookDetails(Session session, Scan scan) {

        FetchBookDetailsCommandDto command = FetchBookDetailsCommandDto.forScan(session.getId(), scan.getId());
        rabbitTemplate.convertAndSend(
                fetchBookCommandExchangeName,
                fetchBookCommandRetryQueueName,
                command);
        log.info("Sent command to fetch book details for ScanId {}", scan.getId());
    }
}
