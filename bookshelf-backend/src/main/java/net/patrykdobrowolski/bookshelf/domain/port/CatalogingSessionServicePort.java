package net.patrykdobrowolski.bookshelf.domain.port;

import jakarta.transaction.Transactional;
import net.patrykdobrowolski.bookshelf.domain.exception.CatalogingSessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.CatalogingSession;

import java.util.UUID;

public interface CatalogingSessionServicePort {
    @Transactional
    CatalogingSession findById(UUID sessionId) throws CatalogingSessionNotFoundException;

    @Transactional
    CatalogingSession save(CatalogingSession catalogingSession);

    @Transactional
    CatalogingSession createSession();

    @Transactional
    void ensureSessionExists(UUID sessionId) throws CatalogingSessionNotFoundException;
}
