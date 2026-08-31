package net.patrykdobrowolski.bookscanner.domain.port;

import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.domain.model.value.BookDetails;

import java.util.List;
import java.util.UUID;

public interface ScanServicePort {

    void retryScan(UUID sessionId, UUID scanId) throws ScanNotFoundException, SessionNotFoundException;
    List<Scan> getScans(UUID sessionId) throws SessionNotFoundException;
    Scan createScan(UUID sessionId, String isbn) throws SessionNotFoundException;
    Scan updateScan(UUID sessionId, UUID scanId, BookDetails newDetails) throws ScanNotFoundException, SessionNotFoundException;
    void deleteScans(UUID sessionId, List<UUID> scanIds) throws SessionNotFoundException;

}
