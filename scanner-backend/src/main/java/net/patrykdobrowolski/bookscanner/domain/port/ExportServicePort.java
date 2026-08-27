package net.patrykdobrowolski.bookscanner.domain.port;

import jakarta.transaction.Transactional;
import net.patrykdobrowolski.bookscanner.domain.exception.ExportAlreadyRequestedException;
import net.patrykdobrowolski.bookscanner.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.Export;
import net.patrykdobrowolski.bookscanner.domain.model.Session;
import net.patrykdobrowolski.bookscanner.domain.model.command.ExportSessionCommand;

import java.util.UUID;

public interface ExportServicePort {

    @Transactional
    Export requestExport(UUID sessionId, ExportSessionCommand command) throws SessionNotFoundException, ExportAlreadyRequestedException;

    @Transactional
    Session beginExport(UUID sessionId) throws SessionNotFoundException, ExportNotRequestedException;

    @Transactional
    Export findExport(UUID sessionId) throws SessionNotFoundException, ExportNotRequestedException;
}
