package net.patrykdobrowolski.bookscanner.rabbitmq;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.domain.model.Modifier;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.domain.port.BookDetailsFetcherPort;
import net.patrykdobrowolski.bookscanner.rabbitmq.dto.FetchBookDetailsCommandDto;
import net.patrykdobrowolski.bookscanner.service.ScanService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
@Slf4j
@Named
@RequiredArgsConstructor
public class FetchBookDetailsCommandListener {

    private final BookDetailsFetcherPort bookDetailsFetcher;
    private final ScanService scanService;

    @RabbitHandler
    public void handleFetchBookDetailsCommand(FetchBookDetailsCommandDto command) {
        try {
            BookDetails bookDetails = bookDetailsFetcher.fetchBookDetails(command.getScanId(), new ISBN(command.getIsbn()));
            Scan scan = scanService.findScan(command.getScanId());
            if (bookDetails != null) {
                scan.setBookDetails(bookDetails, Modifier.SYSTEM);
            } else {
                scan.markFailed();
            }
            scanService.save(scan);
        } catch (ScanNotFoundException e) {
            log.error("Scan not found", e);
            throw new AmqpRejectAndDontRequeueException(e.getMessage());
        }
    }
}
