package net.patrykdobrowolski.bookscanner.domain.port;

import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;

import java.util.List;
import java.util.UUID;

public interface ScanRepositoryPort {

    Scan findById(UUID scanId) throws ScanNotFoundException;
    Scan save(Scan scan);
    List<Scan> findBySessionId(UUID sessionId);

}
