package net.patrykdobrowolski.bookshelf.domain.port;

import jakarta.transaction.Transactional;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportAlreadyRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.Export;
import net.patrykdobrowolski.bookshelf.domain.model.Session;
import net.patrykdobrowolski.bookshelf.domain.model.command.ExportSessionCommand;

import java.util.UUID;

public interface ExportServicePort {

    @Transactional
    Export requestExport(UUID sessionId, ExportSessionCommand command) throws SessionNotFoundException, ExportAlreadyRequestedException;

    @Transactional
    Session beginExport(UUID sessionId) throws SessionNotFoundException, ExportNotRequestedException;

    @Transactional
    Export findExport(UUID sessionId) throws SessionNotFoundException, ExportNotRequestedException;
}
