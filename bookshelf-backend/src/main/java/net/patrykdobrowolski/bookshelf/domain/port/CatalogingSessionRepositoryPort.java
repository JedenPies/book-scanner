package net.patrykdobrowolski.bookshelf.domain.port;

import net.patrykdobrowolski.bookshelf.domain.exception.CatalogingSessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.CatalogingSession;

import java.util.UUID;

public interface CatalogingSessionRepositoryPort {

    CatalogingSession save(CatalogingSession catalogingSession);
    CatalogingSession findById(UUID sessionId) throws CatalogingSessionNotFoundException;
}
