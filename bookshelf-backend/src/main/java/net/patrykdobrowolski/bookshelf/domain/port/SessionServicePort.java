package net.patrykdobrowolski.bookshelf.domain.port;

import jakarta.transaction.Transactional;
import net.patrykdobrowolski.bookshelf.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.Session;

import java.util.UUID;

public interface SessionServicePort {
    @Transactional
    Session findById(UUID sessionId) throws SessionNotFoundException;

    @Transactional
    Session save(Session session);

    @Transactional
    Session createSession();

    @Transactional
    void ensureSessionExists(UUID sessionId) throws SessionNotFoundException;
}
