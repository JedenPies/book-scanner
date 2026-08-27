package net.patrykdobrowolski.bookscanner.service;

import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.model.command.ExportSessionCommand;
import net.patrykdobrowolski.bookscanner.domain.model.event.ExportRequestedEvent;
import net.patrykdobrowolski.bookscanner.domain.model.event.ScanCreatedEvent;
import net.patrykdobrowolski.bookscanner.domain.model.event.ScanDeletedEvent;
import net.patrykdobrowolski.bookscanner.domain.model.event.ScanUpdatedEvent;
import net.patrykdobrowolski.bookscanner.domain.exception.ExportAlreadyRequestedException;
import net.patrykdobrowolski.bookscanner.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.Export;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.domain.model.Session;
import net.patrykdobrowolski.bookscanner.domain.port.*;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
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
