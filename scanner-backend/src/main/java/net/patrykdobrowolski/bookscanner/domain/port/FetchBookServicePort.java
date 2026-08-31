package net.patrykdobrowolski.bookscanner.domain.port;

import net.patrykdobrowolski.bookscanner.domain.exception.DraftBookNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.DraftBookStatus;

import java.util.UUID;

public interface FetchBookServicePort {

    DraftBookStatus fetchBookForScan(UUID sessionId, UUID scanId, boolean lastTry) throws DraftBookNotFoundException, SessionNotFoundException;
}
