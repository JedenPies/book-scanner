package net.patrykdobrowolski.bookscanner.domain.port;

import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.Session;

import java.util.UUID;

public interface SessionRepositoryPort {

    Session save(Session session);
    Session findById(UUID sessionId) throws SessionNotFoundException;
}
