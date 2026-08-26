package net.patrykdobrowolski.bookscanner.adapter.exporter;

import net.patrykdobrowolski.bookscanner.domain.model.ExportFormat;
import net.patrykdobrowolski.bookscanner.domain.model.Session;

public interface SessionExporter {

    boolean supports(ExportFormat format);
    ExportResult export(Session session) throws ExportFailedException;
}
