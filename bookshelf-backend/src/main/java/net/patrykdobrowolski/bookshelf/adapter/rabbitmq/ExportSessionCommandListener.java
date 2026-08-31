package net.patrykdobrowolski.bookshelf.adapter.rabbitmq;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.CatalogingSessionNotFoundException;
import net.patrykdobrowolski.bookshelf.adapter.rabbitmq.dto.ExportSessionCommandDto;
import net.patrykdobrowolski.bookshelf.service.ExportCatalogingSessionService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = "${rabbitmq.export-session-command-queue}")
@Slf4j
@Named
@RequiredArgsConstructor
public class ExportSessionCommandListener {

    private final ExportCatalogingSessionService exportCatalogingSessionService;

    @RabbitHandler
    public void handleFetchBookDetailsCommand(ExportSessionCommandDto command) throws CatalogingSessionNotFoundException, ExportNotRequestedException {
        exportCatalogingSessionService.exportCatalogingSession(command.getSessionId());
    }
}
