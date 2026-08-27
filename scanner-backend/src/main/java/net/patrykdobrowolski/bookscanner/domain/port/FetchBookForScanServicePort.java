package net.patrykdobrowolski.bookscanner.domain.port;

import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.ScanStatus;

import java.util.UUID;

public interface FetchBookForScanServicePort {

    ScanStatus fetchBookForScan(UUID sessionId, UUID scanId, boolean lastTry) throws ScanNotFoundException, SessionNotFoundException;
}
