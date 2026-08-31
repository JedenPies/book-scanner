package net.patrykdobrowolski.bookshelf.adapter.rabbitmq;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookshelf.domain.model.export.Export;
import net.patrykdobrowolski.bookshelf.domain.port.ExportCreatorAsyncPort;
import net.patrykdobrowolski.bookshelf.adapter.rabbitmq.dto.ExportCommandDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

@Named
@RequiredArgsConstructor
@Slf4j
public class ExportCreatorAsyncAdapter implements ExportCreatorAsyncPort {

    @Value("${rabbitmq.command-exchange}")
    private String commandExchangeName;

    @Value("${rabbitmq.export-command-queue}")
    private String exportSessionCommandRetryQueueName;

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void export(Export export) {

        ExportCommandDto command = ExportCommandDto.of(export.getId());
        rabbitTemplate.convertAndSend(
                commandExchangeName,
                exportSessionCommandRetryQueueName,
                command);
        log.info("Sent export command for export {}", export.getId());

    }
}
