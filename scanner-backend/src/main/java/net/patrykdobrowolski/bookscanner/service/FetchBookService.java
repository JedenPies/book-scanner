package net.patrykdobrowolski.bookscanner.service;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.adapter.fetcher.BookDetailsComposer;
import net.patrykdobrowolski.bookscanner.domain.model.event.DraftBookUpdatedEvent;
import net.patrykdobrowolski.bookscanner.domain.exception.DraftBookNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.*;
import net.patrykdobrowolski.bookscanner.domain.model.value.BookDetails;
import net.patrykdobrowolski.bookscanner.domain.port.BookDetailsFetcherPort;
import net.patrykdobrowolski.bookscanner.adapter.fetcher.BookRawResultMapperAdapter;
import net.patrykdobrowolski.bookscanner.domain.port.FetchBookServicePort;
import net.patrykdobrowolski.bookscanner.domain.port.SessionServicePort;
import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

@Named
@RequiredArgsConstructor
public class FetchBookService implements FetchBookServicePort {

    private final SessionServicePort sessionService;
    private final BookDetailsFetcherPort bookDetailsFetcher;
    private final BookRawResultMapperAdapter mapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public DraftBookStatus fetchBookForScan(UUID sessionId, UUID scanId, boolean lastTry) throws DraftBookNotFoundException, SessionNotFoundException {
        Session session = sessionService.findById(sessionId);
        DraftBook draftBook = session.markScanFetching(scanId);
        saveAndPublish(draftBook, session);
        try {
            tryFetchBook(session, draftBook, lastTry);
        } catch (Exception e) {
            session.markScanFailed(scanId);
        }
        saveAndPublish(draftBook, session);
        return draftBook.getStatus();
    }

    private void saveAndPublish(DraftBook draftBook, Session session) {
        sessionService.save(session);
        eventPublisher.publishEvent(DraftBookUpdatedEvent.of(session, draftBook));
    }

    private void tryFetchBook(Session session, DraftBook draftBook, boolean lastTry) throws DraftBookNotFoundException {
        Book book = bookDetailsFetcher.fetchBookDetails(draftBook.getIsbn());
        FetchResult fetchResult = book.getFetchResult();
        switch (fetchResult) {
            case SUCCESS:
                BookDetails details = new BookDetailsComposer(book.getBookRaws().stream().filter(br -> br.getFetchResult() == FetchResult.SUCCESS).map(mapper::map).toList()).compose();
                session.setScanBookDetails(draftBook.getId(), details, Modifier.SYSTEM);
                break;
            case NOT_FOUND:
                session.markScanNotFound(draftBook.getId());
                break;
            case FAILURE:
                if (lastTry) session.markScanFailed(draftBook.getId());
                break;

        }
    }
}
