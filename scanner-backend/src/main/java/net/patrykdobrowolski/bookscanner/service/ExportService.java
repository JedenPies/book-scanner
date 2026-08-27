package net.patrykdobrowolski.bookscanner.service;

import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.exception.ExportAlreadyRequestedException;
import net.patrykdobrowolski.bookscanner.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.Export;
import net.patrykdobrowolski.bookscanner.domain.model.Session;
import net.patrykdobrowolski.bookscanner.domain.model.command.ExportSessionCommand;
import net.patrykdobrowolski.bookscanner.domain.model.event.ExportRequestedEvent;
import net.patrykdobrowolski.bookscanner.domain.port.ExportServicePort;
import net.patrykdobrowolski.bookscanner.domain.port.SessionRepositoryPort;
import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

@Named
@RequiredArgsConstructor
public class ExportService implements ExportServicePort {

    private final SessionRepositoryPort sessionRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public Export requestExport(UUID sessionId, ExportSessionCommand command) throws SessionNotFoundException, ExportAlreadyRequestedException {
        Session session = sessionRepository.findById(sessionId);
        Export export = session.requestExport(command);
        sessionRepository.save(session);
        eventPublisher.publishEvent(ExportRequestedEvent.of(session));
        return export;
    }

    @Transactional
    @Override
    public Session beginExport(UUID sessionId) throws SessionNotFoundException, ExportNotRequestedException {
        Session session = sessionRepository.findById(sessionId);
        session.beginExport();
        return sessionRepository.save(session);
    }

    @Transactional
    @Override
    public Export findExport(UUID sessionId) throws SessionNotFoundException, ExportNotRequestedException {
        Session session = sessionRepository.findById(sessionId);
        Export export = session.getExport();
        if (export == null) throw new ExportNotRequestedException();
        return export;
    }
}
