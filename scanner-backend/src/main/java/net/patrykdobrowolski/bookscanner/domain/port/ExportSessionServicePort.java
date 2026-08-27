package net.patrykdobrowolski.bookscanner.domain.port;

import net.patrykdobrowolski.bookscanner.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;

import java.util.UUID;

public interface ExportSessionServicePort {

    void exportSession(UUID sessionId) throws SessionNotFoundException, ExportNotRequestedException;
}
