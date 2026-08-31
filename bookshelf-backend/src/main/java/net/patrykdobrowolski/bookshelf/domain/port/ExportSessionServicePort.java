package net.patrykdobrowolski.bookshelf.domain.port;

import net.patrykdobrowolski.bookshelf.domain.exception.ExportNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.CatalogingSessionNotFoundException;

import java.util.UUID;

public interface ExportSessionServicePort {

    void doExport(UUID sessionId) throws CatalogingSessionNotFoundException, ExportNotRequestedException, ExportNotFoundException;
}
