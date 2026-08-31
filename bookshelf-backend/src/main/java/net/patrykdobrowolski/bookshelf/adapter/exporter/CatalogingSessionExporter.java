package net.patrykdobrowolski.bookshelf.adapter.exporter;

import net.patrykdobrowolski.bookshelf.domain.model.cataloging.CatalogingSession;
import net.patrykdobrowolski.bookshelf.domain.model.value.ExportFormat;

public interface CatalogingSessionExporter {

    boolean supports(ExportFormat format);
    ExportResult export(CatalogingSession catalogingSession) throws ExportFailedException;
}
