package net.patrykdobrowolski.bookshelf.domain.port;

import net.patrykdobrowolski.bookshelf.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.Session;

import java.util.UUID;

public interface SessionRepositoryPort {

    Session save(Session session);
    Session findById(UUID sessionId) throws SessionNotFoundException;
}
