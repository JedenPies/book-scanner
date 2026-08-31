package net.patrykdobrowolski.bookscanner.adapter.rabbitmq;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookscanner.domain.exception.DraftBookNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.DraftBookStatus;
import net.patrykdobrowolski.bookscanner.adapter.rabbitmq.dto.FetchBookDetailsCommandDto;
import net.patrykdobrowolski.bookscanner.service.FetchBookService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

@RabbitListener(queues = "${rabbitmq.fetch-book-command-queue}")
@Slf4j
@Named
@RequiredArgsConstructor
public class FetchBookDetailsCommandListener {

    @Value("${rabbitmq.command-exchange}")
    private String fetchBookCommandExchangeName;

    @Value("${rabbitmq.fetch-book-command-retry-queue}")
    private String fetchBookCommandRetryQueueName;

    private final FetchBookService fetchBookService;
    private final RabbitTemplate rabbitTemplate;

    @RabbitHandler
    public void handleFetchBookDetailsCommand(FetchBookDetailsCommandDto command) {
        try {
            command.tried();
            DraftBookStatus draftBookStatus = fetchBookService.fetchBookForScan(command.getSessionId(), command.getScanId(), command.getTryCount() >= 3);
            if (draftBookStatus == DraftBookStatus.FETCHING) retry(command);
        } catch (DraftBookNotFoundException e) {
            log.error("Scan not found", e);
            throw new AmqpRejectAndDontRequeueException(e.getMessage());
        } catch (SessionNotFoundException e) {
            log.error("Session not found", e);
            throw new AmqpRejectAndDontRequeueException(e.getMessage());
        }
    }

    private void retry(FetchBookDetailsCommandDto command) {
        log.warn("Nie udało się pobrać danych dla sesji {} scan {}. Próba: {}/3",
                command.getSessionId(), command.getScanId(), command.getTryCount() + 1);
        if (command.getTryCount() < 3) {
            rabbitTemplate.convertAndSend(fetchBookCommandExchangeName, fetchBookCommandRetryQueueName, command);
        } else {
            log.error("Wyczerpano 3 próby pobrania dla sesji {} scan {}", command.getSessionId(), command.getScanId());
        }
    }
}
