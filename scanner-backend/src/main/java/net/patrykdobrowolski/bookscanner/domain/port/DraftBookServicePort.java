package net.patrykdobrowolski.bookscanner.domain.port;

import net.patrykdobrowolski.bookscanner.domain.exception.DraftBookNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.DraftBook;
import net.patrykdobrowolski.bookscanner.domain.model.value.BookDetails;

import java.util.List;
import java.util.UUID;

public interface DraftBookServicePort {

    void retryScan(UUID sessionId, UUID scanId) throws DraftBookNotFoundException, SessionNotFoundException;
    List<DraftBook> getScans(UUID sessionId) throws SessionNotFoundException;
    DraftBook createScan(UUID sessionId, String isbn) throws SessionNotFoundException;
    DraftBook updateScan(UUID sessionId, UUID scanId, BookDetails newDetails) throws DraftBookNotFoundException, SessionNotFoundException;
    void deleteScans(UUID sessionId, List<UUID> scanIds) throws SessionNotFoundException;

}
