package net.patrykdobrowolski.bookshelf.service;

import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.exception.DraftBookNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.DraftBook;
import net.patrykdobrowolski.bookshelf.domain.model.ISBN;
import net.patrykdobrowolski.bookshelf.domain.model.Session;
import net.patrykdobrowolski.bookshelf.domain.model.event.DraftBookCreatedEvent;
import net.patrykdobrowolski.bookshelf.domain.model.event.DraftBookUpdatedEvent;
import net.patrykdobrowolski.bookshelf.domain.model.event.DraftBooksDeletedEvent;
import net.patrykdobrowolski.bookshelf.domain.model.value.BookDetails;
import net.patrykdobrowolski.bookshelf.domain.port.BookDetailsAsyncFetcherPort;
import net.patrykdobrowolski.bookshelf.domain.port.DraftBookServicePort;
import net.patrykdobrowolski.bookshelf.domain.port.SessionRepositoryPort;
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
    public void retryDraftBookFetch(UUID sessionId, UUID draftBookId) throws DraftBookNotFoundException, SessionNotFoundException {
        Session session = sessionRepository.findById(sessionId);
        DraftBook draftBook = session.findDraftBookById(draftBookId);
        eventPublisher.publishEvent(DraftBookUpdatedEvent.of(session, draftBook));
        bookDetailsFetcher.fetchBookDetails(session, draftBook);
    }

    @Transactional
    @Override
    public List<DraftBook> getDraftBooks(UUID sessionId) throws SessionNotFoundException {
        Session session = sessionRepository.findById(sessionId);
        return session.getDraftBooks();
    }

    @Transactional
    @Override
    public DraftBook createDraftBook(UUID sessionId, String isbn) throws SessionNotFoundException {
        Session session = sessionRepository.findById(sessionId);
        DraftBook newDraftBook = session.createNewDraftBook(new ISBN(isbn));
        Session saved = sessionRepository.save(session);
        eventPublisher.publishEvent(DraftBookCreatedEvent.of(saved, newDraftBook));
        return newDraftBook;

    }

    @Transactional
    @Override
    public DraftBook updateDraftBook(UUID sessionId, UUID draftBookId, BookDetails newDetails) throws DraftBookNotFoundException, SessionNotFoundException {
        Session session = sessionRepository.findById(sessionId);
        DraftBook result = session.updateDraftBook(draftBookId, newDetails);
        sessionRepository.save(session);
        return result;
    }

    @Override
    public void deleteDraftBooks(UUID sessionId, List<UUID> draftBookIds) throws SessionNotFoundException {
        Session session = sessionRepository.findById(sessionId);
        List<DraftBook> draftBooks = session.removeDraftBooks(draftBookIds);
        if (draftBooks.isEmpty()) return;
        sessionRepository.save(session);
        eventPublisher.publishEvent(DraftBooksDeletedEvent.of(session, draftBooks));
    }
}
