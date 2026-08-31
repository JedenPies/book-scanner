package net.patrykdobrowolski.bookshelf.service;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.BookDetailsComposer;
import net.patrykdobrowolski.bookshelf.domain.model.cataloging.CatalogingSession;
import net.patrykdobrowolski.bookshelf.domain.model.cataloging.DraftBook;
import net.patrykdobrowolski.bookshelf.domain.model.event.DraftBookUpdatedEvent;
import net.patrykdobrowolski.bookshelf.domain.exception.DraftBookNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.exception.CatalogingSessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.*;
import net.patrykdobrowolski.bookshelf.domain.model.value.BookDetails;
import net.patrykdobrowolski.bookshelf.domain.model.value.DraftBookStatus;
import net.patrykdobrowolski.bookshelf.domain.model.value.FetchResult;
import net.patrykdobrowolski.bookshelf.domain.model.value.Modifier;
import net.patrykdobrowolski.bookshelf.domain.port.BookDetailsFetcherPort;
import net.patrykdobrowolski.bookshelf.adapter.fetcher.BookRawResultMapperAdapter;
import net.patrykdobrowolski.bookshelf.domain.port.FetchBookServicePort;
import net.patrykdobrowolski.bookshelf.domain.port.CatalogingSessionServicePort;
import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

@Named
@RequiredArgsConstructor
public class FetchBookService implements FetchBookServicePort {

    private final CatalogingSessionServicePort sessionService;
    private final BookDetailsFetcherPort bookDetailsFetcher;
    private final BookRawResultMapperAdapter mapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public DraftBookStatus fetchBookForDraft(UUID sessionId, UUID draftBookId, boolean lastTry) throws DraftBookNotFoundException, CatalogingSessionNotFoundException {
        CatalogingSession catalogingSession = sessionService.findById(sessionId);
        DraftBook draftBook = catalogingSession.markDraftBookFetching(draftBookId);
        saveAndPublish(draftBook, catalogingSession);
        try {
            tryFetchBook(catalogingSession, draftBook, lastTry);
        } catch (Exception e) {
            catalogingSession.markDraftBookFailed(draftBookId);
        }
        saveAndPublish(draftBook, catalogingSession);
        return draftBook.getStatus();
    }

    private void saveAndPublish(DraftBook draftBook, CatalogingSession catalogingSession) {
        sessionService.save(catalogingSession);
        eventPublisher.publishEvent(DraftBookUpdatedEvent.of(catalogingSession, draftBook));
    }

    private void tryFetchBook(CatalogingSession catalogingSession, DraftBook draftBook, boolean lastTry) throws DraftBookNotFoundException {
        Book book = bookDetailsFetcher.fetchBookDetails(draftBook.getIsbn());
        FetchResult fetchResult = book.getFetchResult();
        switch (fetchResult) {
            case SUCCESS:
                BookDetails details = new BookDetailsComposer(book.getBookRaws().stream().filter(br -> br.getFetchResult() == FetchResult.SUCCESS).map(mapper::map).toList()).compose();
                catalogingSession.setDraftBookBookDetails(draftBook.getId(), details, Modifier.SYSTEM);
                break;
            case NOT_FOUND:
                catalogingSession.markDraftBookNotFound(draftBook.getId());
                break;
            case FAILURE:
                if (lastTry) catalogingSession.markDraftBookFailed(draftBook.getId());
                break;

        }
    }
}
