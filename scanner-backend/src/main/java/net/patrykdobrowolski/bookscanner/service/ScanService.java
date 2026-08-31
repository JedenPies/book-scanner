package net.patrykdobrowolski.bookscanner.service;

import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.domain.model.Session;
import net.patrykdobrowolski.bookscanner.domain.model.event.ScanCreatedEvent;
import net.patrykdobrowolski.bookscanner.domain.model.event.ScanUpdatedEvent;
import net.patrykdobrowolski.bookscanner.domain.model.event.ScansDeletedEvent;
import net.patrykdobrowolski.bookscanner.domain.model.value.BookDetails;
import net.patrykdobrowolski.bookscanner.domain.port.BookDetailsAsyncFetcherPort;
import net.patrykdobrowolski.bookscanner.domain.port.ScanServicePort;
import net.patrykdobrowolski.bookscanner.domain.port.SessionRepositoryPort;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.UUID;

@Named
@RequiredArgsConstructor
public class ScanService implements ScanServicePort {

    private final SessionRepositoryPort sessionRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final BookDetailsAsyncFetcherPort bookDetailsFetcher;

    @Transactional
    @Override
    public void retryScan(UUID sessionId, UUID scanId) throws ScanNotFoundException, SessionNotFoundException {
        Session session = sessionRepository.findById(sessionId);
        Scan scan = session.findScanById(scanId);
        eventPublisher.publishEvent(ScanUpdatedEvent.of(session, scan));
        bookDetailsFetcher.fetchBookDetails(session, scan);
    }

    @Transactional
    @Override
    public List<Scan> getScans(UUID sessionId) throws SessionNotFoundException {
        Session session = sessionRepository.findById(sessionId);
        return session.getScans();
    }

    @Transactional
    @Override
    public Scan createScan(UUID sessionId, String isbn) throws SessionNotFoundException {
        Session session = sessionRepository.findById(sessionId);
        Scan newScan = session.createNewScan(new ISBN(isbn));
        Session saved = sessionRepository.save(session);
        eventPublisher.publishEvent(ScanCreatedEvent.of(saved, newScan));
        return newScan;

    }

    @Transactional
    @Override
    public Scan updateScan(UUID sessionId, UUID scanId, BookDetails newDetails) throws ScanNotFoundException, SessionNotFoundException {
        Session session = sessionRepository.findById(sessionId);
        Scan result = session.updateScan(scanId, newDetails);
        sessionRepository.save(session);
        return result;
    }

    @Override
    public void deleteScans(UUID sessionId, List<UUID> scanIds) throws SessionNotFoundException {
        Session session = sessionRepository.findById(sessionId);
        List<Scan> scans = session.removeScans(scanIds);
        if (scans.isEmpty()) return;
        sessionRepository.save(session);
        eventPublisher.publishEvent(ScansDeletedEvent.of(session, scans));
    }
}
