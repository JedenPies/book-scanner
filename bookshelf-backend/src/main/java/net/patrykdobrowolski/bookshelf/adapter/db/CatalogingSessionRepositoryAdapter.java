package net.patrykdobrowolski.bookshelf.adapter.db;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.adapter.db.entity.CatalogingSessionEntity;
import net.patrykdobrowolski.bookshelf.adapter.db.mapper.CatalogingSessionEntityMapper;
import net.patrykdobrowolski.bookshelf.adapter.db.repository.SpringDataSessionRepository;
import net.patrykdobrowolski.bookshelf.domain.exception.CatalogingSessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.CatalogingSession;
import net.patrykdobrowolski.bookshelf.domain.port.CatalogingSessionRepositoryPort;

import java.util.UUID;

@Named
@RequiredArgsConstructor
public class CatalogingSessionRepositoryAdapter implements CatalogingSessionRepositoryPort {

    private final SpringDataSessionRepository sessionRepository;
    private final CatalogingSessionEntityMapper catalogingSessionEntityMapper;

    @Override
    public CatalogingSession save(CatalogingSession catalogingSession) {
        CatalogingSessionEntity saved = sessionRepository.save(catalogingSessionEntityMapper.toEntity(catalogingSession));
        return catalogingSessionEntityMapper.fromEntity(saved);
    }

    @Override
    public CatalogingSession findById(UUID sessionId) throws CatalogingSessionNotFoundException {
        return sessionRepository.findById(sessionId)
                .map(catalogingSessionEntityMapper::fromEntity)
                .orElseThrow(() -> CatalogingSessionNotFoundException.of(sessionId));
    }
}
