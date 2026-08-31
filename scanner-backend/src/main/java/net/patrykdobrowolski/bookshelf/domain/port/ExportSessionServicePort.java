package net.patrykdobrowolski.bookshelf.domain.port;

import net.patrykdobrowolski.bookshelf.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.SessionNotFoundException;

import java.util.UUID;

public interface ExportSessionServicePort {

    void exportSession(UUID sessionId) throws SessionNotFoundException, ExportNotRequestedException;
}
