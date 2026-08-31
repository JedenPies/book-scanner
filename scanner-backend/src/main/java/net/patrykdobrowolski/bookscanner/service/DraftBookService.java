package net.patrykdobrowolski.bookscanner.service;

import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.exception.DraftBookNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.DraftBook;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.domain.model.Session;
import net.patrykdobrowolski.bookscanner.domain.model.event.DraftBookCreatedEvent;
import net.patrykdobrowolski.bookscanner.domain.model.event.DraftBookUpdatedEvent;
import net.patrykdobrowolski.bookscanner.domain.model.event.DraftBooksDeletedEvent;
import net.patrykdobrowolski.bookscanner.domain.model.value.BookDetails;
import net.patrykdobrowolski.bookscanner.domain.port.BookDetailsAsyncFetcherPort;
import net.patrykdobrowolski.bookscanner.domain.port.DraftBookServicePort;
import net.patrykdobrowolski.bookscanner.domain.port.SessionRepositoryPort;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.UUID;

@Named
@RequiredArgsConstructor
public class DraftBookService implements DraftBookServicePort {

    private final SessionRepositoryPort sessionRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final BookDetailsAsyncFetcherPort bookDetailsFetcher;

    @Transactional
    @Override
    public void retryScan(UUID sessionId, UUID scanId) throws DraftBookNotFoundException, SessionNotFoundException {
        Session session = sessionRepository.findById(sessionId);
        DraftBook draftBook = session.findScanById(scanId);
        eventPublisher.publishEvent(DraftBookUpdatedEvent.of(session, draftBook));
        bookDetailsFetcher.fetchBookDetails(session, draftBook);
    }

    @Transactional
    @Override
    public List<DraftBook> getScans(UUID sessionId) throws SessionNotFoundException {
        Session session = sessionRepository.findById(sessionId);
        return session.getDraftBooks();
    }

    @Transactional
    @Override
    public DraftBook createScan(UUID sessionId, String isbn) throws SessionNotFoundException {
        Session session = sessionRepository.findById(sessionId);
        DraftBook newDraftBook = session.createNewScan(new ISBN(isbn));
        Session saved = sessionRepository.save(session);
        eventPublisher.publishEvent(DraftBookCreatedEvent.of(saved, newDraftBook));
        return newDraftBook;

    }

    @Transactional
    @Override
    public DraftBook updateScan(UUID sessionId, UUID scanId, BookDetails newDetails) throws DraftBookNotFoundException, SessionNotFoundException {
        Session session = sessionRepository.findById(sessionId);
        DraftBook result = session.updateScan(scanId, newDetails);
        sessionRepository.save(session);
        return result;
    }

    @Override
    public void deleteScans(UUID sessionId, List<UUID> scanIds) throws SessionNotFoundException {
        Session session = sessionRepository.findById(sessionId);
        List<DraftBook> draftBooks = session.removeScans(scanIds);
        if (draftBooks.isEmpty()) return;
        sessionRepository.save(session);
        eventPublisher.publishEvent(DraftBooksDeletedEvent.of(session, draftBooks));
    }
}
