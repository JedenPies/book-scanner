package net.patrykdobrowolski.bookscanner.service;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.event.ScanUpdatedEvent;
import net.patrykdobrowolski.bookscanner.domain.exception.CannotFetchBookException;
import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.*;
import net.patrykdobrowolski.bookscanner.domain.port.BookDetailsFetcherPort;
import net.patrykdobrowolski.bookscanner.fetcher.BookRawResultMapperAdapter;
import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

@Named
@RequiredArgsConstructor
public class FetchBookForScanService {

    private final SessionService sessionService;
    private final BookDetailsFetcherPort bookDetailsFetcher;
    private final BookRawResultMapperAdapter mapper;
    private final ApplicationEventPublisher eventPublisher;

    public void fetchBookForScan(UUID sessionId, UUID scanId) throws ScanNotFoundException, SessionNotFoundException {
        Session session = sessionService.findById(sessionId);
        Scan scan = session.findScanById(scanId);
        scan.markFetching();
        sessionService.save(session);
        eventPublisher.publishEvent(ScanUpdatedEvent.builder().scan(scan).session(session).build());
        tryFetchBook(scan);
        sessionService.save(session);
        eventPublisher.publishEvent(ScanUpdatedEvent.builder().scan(scan).session(session).build());
    }

    private void tryFetchBook(Scan scan) {
        try {
            Book book = bookDetailsFetcher.fetchBookDetails(scan.getIsbn());
            FetchResult fetchResult = book.getFetchResult();
            switch (fetchResult) {
                case SUCCESS:
                    scan.setBookDetails(mapper.map(book.getPreferededBookRaw()), Modifier.SYSTEM);
                    break;
                case NOT_FOUND:
                    scan.markNotFound();
                    break;
                case FAILURE:
                    scan.markFailed();
                    break;

            }
        } catch (CannotFetchBookException e) {
            scan.markFailed();
        }
    }
}
