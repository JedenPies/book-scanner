package net.patrykdobrowolski.bookshelf.domain.port;

import jakarta.transaction.Transactional;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportAlreadyRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.CatalogingSessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.CatalogingSession;
import net.patrykdobrowolski.bookshelf.domain.model.Export;
import net.patrykdobrowolski.bookshelf.domain.model.command.ExportSessionCommand;

import java.util.UUID;

public interface ExportServicePort {

    @Transactional
    Export requestExport(UUID sessionId, ExportSessionCommand command) throws CatalogingSessionNotFoundException, ExportAlreadyRequestedException;

    @Transactional
    CatalogingSession beginExport(UUID sessionId) throws CatalogingSessionNotFoundException, ExportNotRequestedException;

    @Transactional
    Export findExport(UUID sessionId) throws CatalogingSessionNotFoundException, ExportNotRequestedException;
}
