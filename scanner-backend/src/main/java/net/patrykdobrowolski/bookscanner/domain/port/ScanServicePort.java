package net.patrykdobrowolski.bookscanner.domain.port;

import jakarta.transaction.Transactional;
import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;

import java.util.List;
import java.util.UUID;

public interface ScanServicePort {
    @Transactional
    void retryScan(UUID sessionId, UUID scanId) throws ScanNotFoundException, SessionNotFoundException;

    @Transactional
    List<Scan> getScans(UUID sessionId) throws SessionNotFoundException;

    @Transactional
    Scan createScan(UUID sessionId, String isbn) throws SessionNotFoundException;

    @Transactional
    void deleteScan(UUID sessionId, UUID scanId) throws SessionNotFoundException, ScanNotFoundException;
}
