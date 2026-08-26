package net.patrykdobrowolski.bookscanner.service;

import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.command.ExportSessionCommand;
import net.patrykdobrowolski.bookscanner.domain.event.ExportRequestedEvent;
import net.patrykdobrowolski.bookscanner.domain.event.ScanCreatedEvent;
import net.patrykdobrowolski.bookscanner.domain.event.ScanDeletedEvent;
import net.patrykdobrowolski.bookscanner.domain.event.ScanUpdatedEvent;
import net.patrykdobrowolski.bookscanner.domain.exception.ExportAlreadyRequestedException;
import net.patrykdobrowolski.bookscanner.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.Export;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.domain.model.Session;
import net.patrykdobrowolski.bookscanner.domain.port.BookDetailsAsyncFetcherPort;
import net.patrykdobrowolski.bookscanner.domain.port.SessionRepositoryPort;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.UUID;

@Named
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepositoryPort sessionRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final BookDetailsAsyncFetcherPort bookDetailsFetcher;

    @Transactional
    public Session findById(UUID sessionId) throws SessionNotFoundException {
        return sessionRepository.findById(sessionId);
    }

    @Transactional
    public Session save(Session session) {
        return sessionRepository.save(session);
    }

    @Transactional
    public Session createSession() {
        Session newSession = Session.createNew();
        return sessionRepository.save(newSession);
    }

    @Transactional
    public void ensureSessionExists(UUID sessionId) throws SessionNotFoundException {
        sessionRepository.findById(sessionId);
    }

    @Transactional
    public void retryScan(UUID sessionId, UUID scanId) throws ScanNotFoundException, SessionNotFoundException {
        Session session = sessionRepository.findById(sessionId);
        Scan scan = session.findScanById(scanId);
        scan.markFetching();
        sessionRepository.save(session);
        eventPublisher.publishEvent(ScanUpdatedEvent.of(session, scan));
        bookDetailsFetcher.fetchBookDetails(session, scan);
    }


    @Transactional
    public List<Scan> getScans(UUID sessionId) throws SessionNotFoundException {
        Session session = sessionRepository.findById(sessionId);
        return session.getScans();
    }

    @Transactional
    public Scan createScan(UUID sessionId, String isbn) throws SessionNotFoundException {
        Session session = sessionRepository.findById(sessionId);
        Scan newScan = session.createNewScan(new ISBN(isbn));
        Session saved = sessionRepository.save(session);
        eventPublisher.publishEvent(ScanCreatedEvent.of(saved, newScan));
        return newScan;

    }

    @Transactional
    public void deleteScan(UUID sessionId, UUID scanId) throws SessionNotFoundException, ScanNotFoundException {
        Session session = sessionRepository.findById(sessionId);
        Scan removedScan = session.removeScan(scanId);
        sessionRepository.save(session);
        eventPublisher.publishEvent(ScanDeletedEvent.of(session, removedScan));
    }

    @Transactional
    public Export requestExport(UUID sessionId, ExportSessionCommand command) throws SessionNotFoundException, ExportAlreadyRequestedException {
        Session session = sessionRepository.findById(sessionId);
        Export export = session.requestExport(command);
        sessionRepository.save(session);
        eventPublisher.publishEvent(ExportRequestedEvent.of(session));
        return export;
    }

    @Transactional
    public Session beginExport(UUID sessionId) throws SessionNotFoundException, ExportNotRequestedException {
        Session session = sessionRepository.findById(sessionId);
        session.beginExport();
        return sessionRepository.save(session);
    }

    @Transactional
    public Export findExport(UUID sessionId) throws SessionNotFoundException, ExportNotRequestedException {
        Session session = sessionRepository.findById(sessionId);
        Export export = session.getExport();
        if (export == null) throw new ExportNotRequestedException();
        return export;
    }
}
