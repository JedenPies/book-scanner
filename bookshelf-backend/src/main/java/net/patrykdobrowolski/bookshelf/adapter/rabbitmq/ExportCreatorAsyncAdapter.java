package net.patrykdobrowolski.bookshelf.adapter.rabbitmq;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookshelf.domain.model.cataloging.CatalogingSession;
import net.patrykdobrowolski.bookshelf.domain.port.ExportCreatorAsyncPort;
import net.patrykdobrowolski.bookshelf.adapter.rabbitmq.dto.ExportSessionCommandDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

@Named
@RequiredArgsConstructor
@Slf4j
public class ExportCreatorAsyncAdapter implements ExportCreatorAsyncPort {

    @Value("${rabbitmq.command-exchange}")
    private String commandExchangeName;

    @Value("${rabbitmq.export-session-command-queue}")
    private String exportSessionCommandRetryQueueName;

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void exportSession(CatalogingSession catalogingSession) {

        ExportSessionCommandDto command = ExportSessionCommandDto.forSession(catalogingSession.getId());
        rabbitTemplate.convertAndSend(
                commandExchangeName,
                exportSessionCommandRetryQueueName,
                command);
        log.info("Sent export command for session {}", catalogingSession.getId());

    }
}
