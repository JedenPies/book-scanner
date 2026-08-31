package net.patrykdobrowolski.bookshelf.adapter.rabbitmq;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookshelf.adapter.rabbitmq.dto.ExportSessionCommandDto;
import net.patrykdobrowolski.bookshelf.service.ExportSessionService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = "${rabbitmq.export-session-command-queue}")
@Slf4j
@Named
@RequiredArgsConstructor
public class ExportSessionCommandListener {

    private final ExportSessionService exportSessionService;

    @RabbitHandler
    public void handleFetchBookDetailsCommand(ExportSessionCommandDto command) throws SessionNotFoundException, ExportNotRequestedException {
        exportSessionService.exportSession(command.getSessionId());
    }
}
