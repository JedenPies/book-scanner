package net.patrykdobrowolski.bookshelf.adapter.db;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.adapter.db.entity.SessionEntity;
import net.patrykdobrowolski.bookshelf.adapter.db.mapper.SessionEntityMapper;
import net.patrykdobrowolski.bookshelf.adapter.db.repository.SpringDataSessionRepository;
import net.patrykdobrowolski.bookshelf.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.Session;
import net.patrykdobrowolski.bookshelf.domain.port.SessionRepositoryPort;

import java.util.UUID;

@Named
@RequiredArgsConstructor
public class SessionRepositoryAdapter implements SessionRepositoryPort {

    private final SpringDataSessionRepository sessionRepository;
    private final SessionEntityMapper sessionEntityMapper;

    @Override
    public Session save(Session session) {
        SessionEntity saved = sessionRepository.save(sessionEntityMapper.toEntity(session));
        return sessionEntityMapper.fromEntity(saved);
    }

    @Override
    public Session findById(UUID sessionId) throws SessionNotFoundException {
        return sessionRepository.findById(sessionId)
                .map(sessionEntityMapper::fromEntity)
                .orElseThrow(() -> SessionNotFoundException.of(sessionId));
    }
}
