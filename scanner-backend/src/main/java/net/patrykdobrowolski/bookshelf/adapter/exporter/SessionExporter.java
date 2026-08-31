package net.patrykdobrowolski.bookshelf.adapter.exporter;

import net.patrykdobrowolski.bookshelf.domain.model.ExportFormat;
import net.patrykdobrowolski.bookshelf.domain.model.Session;

public interface SessionExporter {

    boolean supports(ExportFormat format);
    ExportResult export(Session session) throws ExportFailedException;
}
