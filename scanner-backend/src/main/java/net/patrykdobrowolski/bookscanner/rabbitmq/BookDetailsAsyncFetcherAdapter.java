package net.patrykdobrowolski.bookscanner.rabbitmq;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.domain.port.BookDetailsAsyncFetcherPort;
import net.patrykdobrowolski.bookscanner.rabbitmq.dto.FetchBookDetailsCommandDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

@Named
@RequiredArgsConstructor
@Slf4j
public class BookDetailsAsyncFetcherAdapter implements BookDetailsAsyncFetcherPort {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void fetchBookDetails(UUID scanId, ISBN isbn) {

        FetchBookDetailsCommandDto command = FetchBookDetailsCommandDto.builder()
                .scanId(scanId)
                .isbn(isbn.value())
                .build();
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.EXCHANGE_NAME,
                RabbitMqConfig.ROUTING_KEY,
                command);
        log.info("Sent command to fetch book details for ISBN {}, ScanId {}", isbn, scanId);
    }
}
