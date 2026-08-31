package net.patrykdobrowolski.bookshelf.service;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookshelf.adapter.exporter.ExportFailedException;
import net.patrykdobrowolski.bookshelf.adapter.exporter.ExportFormatNotSupportedException;
import net.patrykdobrowolski.bookshelf.adapter.exporter.ExportResult;
import net.patrykdobrowolski.bookshelf.adapter.exporter.SessionExporter;
import net.patrykdobrowolski.bookshelf.domain.model.event.ExportCompleteEvent;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.ExportFormat;
import net.patrykdobrowolski.bookshelf.domain.model.Session;
import net.patrykdobrowolski.bookshelf.domain.port.ExportServicePort;
import net.patrykdobrowolski.bookshelf.domain.port.ExportSessionServicePort;
import net.patrykdobrowolski.bookshelf.domain.port.SessionServicePort;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.UUID;

@Named
@RequiredArgsConstructor
@Slf4j
public class ExportSessionService implements ExportSessionServicePort {

    private final SessionServicePort sessionService;
    private final ExportServicePort exportService;
    private final ApplicationEventPublisher eventPublisher;
    private final List<SessionExporter> exporters;

    @Override
    public void exportSession(UUID sessionId) throws SessionNotFoundException, ExportNotRequestedException {
        Session session = exportService.beginExport(sessionId);
        try {
            tryToExport(session);
        } catch (ExportFormatNotSupportedException | ExportFailedException e) {
            log.error("Session export failed", e);
            session.exportFailed();
        }
        Session updatedSession = sessionService.save(session);
        eventPublisher.publishEvent(ExportCompleteEvent.of(updatedSession, updatedSession.getExport()));
    }

    private void tryToExport(Session session) throws ExportFormatNotSupportedException, ExportNotRequestedException, ExportFailedException {
        SessionExporter exporter = findExporter(session.getExport().getFormat());
        ExportResult result = exporter.export(session);
        session.exportSucceed(result.getData());
    }

    private SessionExporter findExporter(ExportFormat format) throws ExportFormatNotSupportedException {
        return exporters.stream().filter(exporter -> exporter.supports(format)).findFirst().orElseThrow(() -> new ExportFormatNotSupportedException(format));
    }
}
