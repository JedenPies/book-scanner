package net.patrykdobrowolski.bookshelf.service;

import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.exception.CatalogingSessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.cataloging.CatalogingSession;
import net.patrykdobrowolski.bookshelf.domain.port.CatalogingSessionRepositoryPort;
import net.patrykdobrowolski.bookshelf.domain.port.CatalogingSessionServicePort;

import java.util.UUID;

@Named
@RequiredArgsConstructor
public class CatalogingSessionService implements CatalogingSessionServicePort {

    private final CatalogingSessionRepositoryPort catalogingSessionRepository;

    @Override
    @Transactional
    public CatalogingSession findById(UUID catalogingSessionId) throws CatalogingSessionNotFoundException {
        return catalogingSessionRepository.findById(catalogingSessionId);
    }

    @Override
    @Transactional
    public CatalogingSession save(CatalogingSession catalogingSession) {
        return catalogingSessionRepository.save(catalogingSession);
    }

    @Transactional
    @Override
    public CatalogingSession createSession() {
        CatalogingSession newCatalogingSession = CatalogingSession.createNew();
        return catalogingSessionRepository.save(newCatalogingSession);
    }

    @Transactional
    @Override
    public void ensureSessionExists(UUID catalogingSessionId) throws CatalogingSessionNotFoundException {
        catalogingSessionRepository.findById(catalogingSessionId);
    }
}
