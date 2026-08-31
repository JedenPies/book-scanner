package net.patrykdobrowolski.bookshelf.service;

import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.Session;
import net.patrykdobrowolski.bookshelf.domain.port.SessionRepositoryPort;
import net.patrykdobrowolski.bookshelf.domain.port.SessionServicePort;

import java.util.UUID;

@Named
@RequiredArgsConstructor
public class SessionService implements SessionServicePort {

    private final SessionRepositoryPort sessionRepository;

    @Override
    @Transactional
    public Session findById(UUID sessionId) throws SessionNotFoundException {
        return sessionRepository.findById(sessionId);
    }

    @Override
    @Transactional
    public Session save(Session session) {
        return sessionRepository.save(session);
    }

    @Transactional
    @Override
    public Session createSession() {
        Session newSession = Session.createNew();
        return sessionRepository.save(newSession);
    }

    @Transactional
    @Override
    public void ensureSessionExists(UUID sessionId) throws SessionNotFoundException {
        sessionRepository.findById(sessionId);
    }
}
