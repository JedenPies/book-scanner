package net.patrykdobrowolski.bookshelf.adapter.exporter;

import net.patrykdobrowolski.bookshelf.domain.model.CatalogingSession;
import net.patrykdobrowolski.bookshelf.domain.model.ExportFormat;

public interface CatalogingSessionExporter {

    boolean supports(ExportFormat format);
    ExportResult export(CatalogingSession catalogingSession) throws ExportFailedException;
}
