package net.patrykdobrowolski.bookshelf.domain.port;

import net.patrykdobrowolski.bookshelf.domain.exception.ExportAlreadyRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.CatalogingSessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.command.ExportCommand;
import net.patrykdobrowolski.bookshelf.domain.model.export.Export;

import java.util.UUID;

public interface ExportServicePort {

    Export requestExport(ExportCommand command) throws CatalogingSessionNotFoundException, ExportAlreadyRequestedException, ExportNotFoundException;
    Export beginExport(UUID exportId) throws CatalogingSessionNotFoundException, ExportNotRequestedException, ExportNotFoundException;
    Export findExport(UUID exportId) throws ExportNotFoundException;
    Export findForCatalogingSession(UUID sessionId) throws ExportNotFoundException;
    Export save(Export export);
}
