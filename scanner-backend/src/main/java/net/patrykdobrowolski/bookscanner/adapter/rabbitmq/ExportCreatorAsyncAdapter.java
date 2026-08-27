package net.patrykdobrowolski.bookscanner.adapter.rabbitmq;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookscanner.domain.model.Session;
import net.patrykdobrowolski.bookscanner.domain.port.ExportCreatorAsyncPort;
import net.patrykdobrowolski.bookscanner.adapter.rabbitmq.dto.ExportSessionCommandDto;
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
    public void exportSession(Session session) {

        ExportSessionCommandDto command = ExportSessionCommandDto.forSession(session.getId());
        rabbitTemplate.convertAndSend(
                commandExchangeName,
                exportSessionCommandRetryQueueName,
                command);
        log.info("Sent export command for session {}", session.getId());

    }
}
