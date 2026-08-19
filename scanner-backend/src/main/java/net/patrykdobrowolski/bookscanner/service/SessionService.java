package net.patrykdobrowolski.bookscanner.service;

import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.Session;
import net.patrykdobrowolski.bookscanner.domain.port.SessionRepositoryPort;

import java.util.UUID;

@Named
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepositoryPort sessionRepository;

    @Transactional
    public Session createSession() {
        Session newSession = Session.builder().build();
        return sessionRepository.save(newSession);
    }

    @Transactional
    public void ensureSessionExists(UUID sessionId) throws SessionNotFoundException {
        sessionRepository.findById(sessionId);
    }
}
