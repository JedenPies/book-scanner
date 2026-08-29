package net.patrykdobrowolski.bookscanner.domain.port;

import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.domain.model.command.UpdateScanCommand;

import java.util.List;
import java.util.UUID;

public interface ScanServicePort {

    void retryScan(UUID sessionId, UUID scanId) throws ScanNotFoundException, SessionNotFoundException;
    List<Scan> getScans(UUID sessionId) throws SessionNotFoundException;
    Scan createScan(UUID sessionId, String isbn) throws SessionNotFoundException;
    void deleteScan(UUID sessionId, UUID scanId) throws SessionNotFoundException, ScanNotFoundException;
    Scan updateScan(UUID sessionId, UUID scanId, UpdateScanCommand command) throws ScanNotFoundException, SessionNotFoundException;

}
