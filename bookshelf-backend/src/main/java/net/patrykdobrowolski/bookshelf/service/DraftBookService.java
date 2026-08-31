package net.patrykdobrowolski.bookshelf.service;

import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.exception.DraftBookNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.exception.CatalogingSessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.CatalogingSession;
import net.patrykdobrowolski.bookshelf.domain.model.DraftBook;
import net.patrykdobrowolski.bookshelf.domain.model.ISBN;
import net.patrykdobrowolski.bookshelf.domain.model.event.DraftBookCreatedEvent;
import net.patrykdobrowolski.bookshelf.domain.model.event.DraftBookUpdatedEvent;
import net.patrykdobrowolski.bookshelf.domain.model.event.DraftBooksDeletedEvent;
import net.patrykdobrowolski.bookshelf.domain.model.value.BookDetails;
import net.patrykdobrowolski.bookshelf.domain.port.BookDetailsAsyncFetcherPort;
import net.patrykdobrowolski.bookshelf.domain.port.DraftBookServicePort;
import net.patrykdobrowolski.bookshelf.domain.port.CatalogingSessionRepositoryPort;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.UUID;

@Named
@RequiredArgsConstructor
public class DraftBookService implements DraftBookServicePort {

    private final CatalogingSessionRepositoryPort sessionRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final BookDetailsAsyncFetcherPort bookDetailsFetcher;

    @Transactional
    @Override
    public void retryDraftBookFetch(UUID sessionId, UUID draftBookId) throws DraftBookNotFoundException, CatalogingSessionNotFoundException {
        CatalogingSession catalogingSession = sessionRepository.findById(sessionId);
        DraftBook draftBook = catalogingSession.findDraftBookById(draftBookId);
        eventPublisher.publishEvent(DraftBookUpdatedEvent.of(catalogingSession, draftBook));
        bookDetailsFetcher.fetchBookDetails(catalogingSession, draftBook);
    }

    @Transactional
    @Override
    public List<DraftBook> getDraftBooks(UUID sessionId) throws CatalogingSessionNotFoundException {
        CatalogingSession catalogingSession = sessionRepository.findById(sessionId);
        return catalogingSession.getDraftBooks();
    }

    @Transactional
    @Override
    public DraftBook createDraftBook(UUID sessionId, String isbn) throws CatalogingSessionNotFoundException {
        CatalogingSession catalogingSession = sessionRepository.findById(sessionId);
        DraftBook newDraftBook = catalogingSession.createNewDraftBook(new ISBN(isbn));
        CatalogingSession saved = sessionRepository.save(catalogingSession);
        eventPublisher.publishEvent(DraftBookCreatedEvent.of(saved, newDraftBook));
        return newDraftBook;

    }

    @Transactional
    @Override
    public DraftBook updateDraftBook(UUID sessionId, UUID draftBookId, BookDetails newDetails) throws DraftBookNotFoundException, CatalogingSessionNotFoundException {
        CatalogingSession catalogingSession = sessionRepository.findById(sessionId);
        DraftBook result = catalogingSession.updateDraftBook(draftBookId, newDetails);
        sessionRepository.save(catalogingSession);
        eventPublisher.publishEvent(DraftBookUpdatedEvent.of(catalogingSession, result));
        return result;
    }

    @Override
    public void deleteDraftBooks(UUID sessionId, List<UUID> draftBookIds) throws CatalogingSessionNotFoundException {
        CatalogingSession catalogingSession = sessionRepository.findById(sessionId);
        List<DraftBook> draftBooks = catalogingSession.removeDraftBooks(draftBookIds);
        if (draftBooks.isEmpty()) return;
        sessionRepository.save(catalogingSession);
        eventPublisher.publishEvent(DraftBooksDeletedEvent.of(catalogingSession, draftBooks));
    }
}
