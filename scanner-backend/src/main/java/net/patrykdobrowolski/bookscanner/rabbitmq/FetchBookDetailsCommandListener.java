package net.patrykdobrowolski.bookscanner.rabbitmq;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;
import net.patrykdobrowolski.bookscanner.rabbitmq.dto.FetchBookDetailsCommandDto;
import net.patrykdobrowolski.bookscanner.service.FetchBookForScanService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
@Slf4j
@Named
@RequiredArgsConstructor
public class FetchBookDetailsCommandListener {

    private final FetchBookForScanService fetchBookForScanService;

    @RabbitHandler
    public void handleFetchBookDetailsCommand(FetchBookDetailsCommandDto command) {
        try {
            fetchBookForScanService.fetchBookForScan(command.getScanId());
        } catch (ScanNotFoundException e) {
            log.error("Scan not found", e);
            throw new AmqpRejectAndDontRequeueException(e.getMessage());
        }
    }
}
