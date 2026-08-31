package net.patrykdobrowolski.bookshelf.adapter.rabbitmq;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookshelf.domain.model.DraftBook;
import net.patrykdobrowolski.bookshelf.domain.model.Session;
import net.patrykdobrowolski.bookshelf.domain.port.BookDetailsAsyncFetcherPort;
import net.patrykdobrowolski.bookshelf.adapter.rabbitmq.dto.FetchBookDetailsCommandDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

@Named
@RequiredArgsConstructor
@Slf4j
public class BookDetailsAsyncFetcherAdapter implements BookDetailsAsyncFetcherPort {

    @Value("${rabbitmq.command-exchange}")
    private String commandExchangeName;

    @Value("${rabbitmq.fetch-book-command-queue}")
    private String fetchBookCommandQueueName;

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void fetchBookDetails(Session session, DraftBook draftBook) {

        FetchBookDetailsCommandDto command = FetchBookDetailsCommandDto.forScan(session.getId(), draftBook.getId());
        rabbitTemplate.convertAndSend(
                commandExchangeName,
                fetchBookCommandQueueName,
                command);
        log.debug("Sent command to fetch book details for draftBook with id {}", draftBook.getId());
    }
}
