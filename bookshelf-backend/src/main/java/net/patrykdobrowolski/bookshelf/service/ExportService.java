package net.patrykdobrowolski.bookshelf.service;

import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportAlreadyRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.CatalogingSessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.CatalogingSession;
import net.patrykdobrowolski.bookshelf.domain.model.Export;
import net.patrykdobrowolski.bookshelf.domain.model.command.ExportSessionCommand;
import net.patrykdobrowolski.bookshelf.domain.model.event.ExportRequestedEvent;
import net.patrykdobrowolski.bookshelf.domain.port.ExportServicePort;
import net.patrykdobrowolski.bookshelf.domain.port.CatalogingSessionRepositoryPort;
import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

@Named
@RequiredArgsConstructor
public class ExportService implements ExportServicePort {

    private final CatalogingSessionRepositoryPort sessionRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public Export requestExport(UUID sessionId, ExportSessionCommand command) throws CatalogingSessionNotFoundException, ExportAlreadyRequestedException {
        CatalogingSession catalogingSession = sessionRepository.findById(sessionId);
        Export export = catalogingSession.requestExport(command);
        sessionRepository.save(catalogingSession);
        eventPublisher.publishEvent(ExportRequestedEvent.of(catalogingSession));
        return export;
    }

    @Transactional
    @Override
    public CatalogingSession beginExport(UUID sessionId) throws CatalogingSessionNotFoundException, ExportNotRequestedException {
        CatalogingSession catalogingSession = sessionRepository.findById(sessionId);
        catalogingSession.beginExport();
        return sessionRepository.save(catalogingSession);
    }

    @Transactional
    @Override
    public Export findExport(UUID sessionId) throws CatalogingSessionNotFoundException, ExportNotRequestedException {
        CatalogingSession catalogingSession = sessionRepository.findById(sessionId);
        Export export = catalogingSession.getExport();
        if (export == null) throw new ExportNotRequestedException();
        return export;
    }
}
