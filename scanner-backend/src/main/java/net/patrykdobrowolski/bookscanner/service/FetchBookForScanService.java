package net.patrykdobrowolski.bookscanner.service;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.adapter.fetcher.BookDetailsComposer;
import net.patrykdobrowolski.bookscanner.domain.model.event.ScanUpdatedEvent;
import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.*;
import net.patrykdobrowolski.bookscanner.domain.port.BookDetailsFetcherPort;
import net.patrykdobrowolski.bookscanner.adapter.fetcher.BookRawResultMapperAdapter;
import net.patrykdobrowolski.bookscanner.domain.port.FetchBookForScanServicePort;
import net.patrykdobrowolski.bookscanner.domain.port.SessionServicePort;
import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

@Named
@RequiredArgsConstructor
public class FetchBookForScanService implements FetchBookForScanServicePort {

    private final SessionServicePort sessionService;
    private final BookDetailsFetcherPort bookDetailsFetcher;
    private final BookRawResultMapperAdapter mapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public ScanStatus fetchBookForScan(UUID sessionId, UUID scanId, boolean lastTry) throws ScanNotFoundException, SessionNotFoundException {
        Session session = sessionService.findById(sessionId);
        Scan scan = session.markScanFetching(scanId);
        saveAndPublish(scan, session);
        try {
            tryFetchBook(session, scan, lastTry);
        } catch (Exception e) {
            session.markScanFailed(scanId);
        }
        saveAndPublish(scan, session);
        return scan.getStatus();
    }

    private void saveAndPublish(Scan scan, Session session) {
        sessionService.save(session);
        eventPublisher.publishEvent(ScanUpdatedEvent.of(session, scan));
    }

    private void tryFetchBook(Session session, Scan scan, boolean lastTry) throws ScanNotFoundException {
        Book book = bookDetailsFetcher.fetchBookDetails(scan.getIsbn());
        FetchResult fetchResult = book.getFetchResult();
        switch (fetchResult) {
            case SUCCESS:
                BookDetails details = new BookDetailsComposer(book.getBookRaws().stream().filter(br -> br.getFetchResult() == FetchResult.SUCCESS).map(mapper::map).toList()).compose();
                session.setScanBookDetails(scan.getId(), details, Modifier.SYSTEM);
                break;
            case NOT_FOUND:
                session.markScanNotFound(scan.getId());
                break;
            case FAILURE:
                if (lastTry) session.markScanFailed(scan.getId());
                break;

        }
    }
}
