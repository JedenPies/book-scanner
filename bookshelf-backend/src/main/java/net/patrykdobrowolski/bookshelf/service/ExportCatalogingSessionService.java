package net.patrykdobrowolski.bookshelf.service;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.patrykdobrowolski.bookshelf.adapter.exporter.ExportFailedException;
import net.patrykdobrowolski.bookshelf.adapter.exporter.ExportFormatNotSupportedException;
import net.patrykdobrowolski.bookshelf.adapter.exporter.ExportResult;
import net.patrykdobrowolski.bookshelf.adapter.exporter.CatalogingSessionExporter;
import net.patrykdobrowolski.bookshelf.domain.model.event.ExportCompleteEvent;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.CatalogingSessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.ExportFormat;
import net.patrykdobrowolski.bookshelf.domain.model.CatalogingSession;
import net.patrykdobrowolski.bookshelf.domain.port.ExportServicePort;
import net.patrykdobrowolski.bookshelf.domain.port.ExportSessionServicePort;
import net.patrykdobrowolski.bookshelf.domain.port.CatalogingSessionServicePort;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.UUID;

@Named
@RequiredArgsConstructor
@Slf4j
public class ExportCatalogingSessionService implements ExportSessionServicePort {

    private final CatalogingSessionServicePort sessionService;
    private final ExportServicePort exportService;
    private final ApplicationEventPublisher eventPublisher;
    private final List<CatalogingSessionExporter> exporters;

    @Override
    public void exportCatalogingSession(UUID sessionId) throws CatalogingSessionNotFoundException, ExportNotRequestedException {
        CatalogingSession catalogingSession = exportService.beginExport(sessionId);
        try {
            tryToExport(catalogingSession);
        } catch (ExportFormatNotSupportedException | ExportFailedException e) {
            log.error("Session export failed", e);
            catalogingSession.exportFailed();
        }
        CatalogingSession updatedCatalogingSession = sessionService.save(catalogingSession);
        eventPublisher.publishEvent(ExportCompleteEvent.of(updatedCatalogingSession, updatedCatalogingSession.getExport()));
    }

    private void tryToExport(CatalogingSession catalogingSession) throws ExportFormatNotSupportedException, ExportNotRequestedException, ExportFailedException {
        CatalogingSessionExporter exporter = findExporter(catalogingSession.getExport().getFormat());
        ExportResult result = exporter.export(catalogingSession);
        catalogingSession.exportSucceed(result.getData());
    }

    private CatalogingSessionExporter findExporter(ExportFormat format) throws ExportFormatNotSupportedException {
        return exporters.stream().filter(exporter -> exporter.supports(format)).findFirst().orElseThrow(() -> new ExportFormatNotSupportedException(format));
    }
}
