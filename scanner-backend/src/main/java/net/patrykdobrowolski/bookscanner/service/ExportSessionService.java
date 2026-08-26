package net.patrykdobrowolski.bookscanner.service;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.adapter.exporter.ExportFailedException;
import net.patrykdobrowolski.bookscanner.adapter.exporter.ExportFormatNotSupportedException;
import net.patrykdobrowolski.bookscanner.adapter.exporter.ExportResult;
import net.patrykdobrowolski.bookscanner.adapter.exporter.SessionExporter;
import net.patrykdobrowolski.bookscanner.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.ExportFormat;
import net.patrykdobrowolski.bookscanner.domain.model.Session;

import java.util.List;
import java.util.UUID;

@Named
@RequiredArgsConstructor
public class ExportSessionService {

    private final SessionService sessionService;
    private final List<SessionExporter> exporters;

    public void exportSession(UUID sessionId) throws SessionNotFoundException, ExportNotRequestedException {
        Session session = sessionService.beginExport(sessionId);
        try {
            tryToExport(session);
        } catch (ExportFormatNotSupportedException | ExportFailedException e) {
            session.exportFailed();
        }
        sessionService.save(session);
    }

    private void tryToExport(Session session) throws ExportFormatNotSupportedException, ExportNotRequestedException, ExportFailedException {
        SessionExporter exporter = findExporter(session.getExport().getFormat());
        ExportResult result = exporter.export(session);
        session.exportCompleted(result.getData());
    }

    private SessionExporter findExporter(ExportFormat format) throws ExportFormatNotSupportedException {
        return exporters.stream().filter(exporter -> exporter.supports(format)).findFirst().orElseThrow(ExportFormatNotSupportedException::new);
    }
}
