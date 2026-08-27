package net.patrykdobrowolski.bookscanner.service;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
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
        Scan scan = session.findScanById(scanId);
        scan.markFetching();
        sessionService.save(session);
        eventPublisher.publishEvent(ScanUpdatedEvent.of(session, scan));
        tryFetchBook(scan, lastTry);
        sessionService.save(session);
        eventPublisher.publishEvent(ScanUpdatedEvent.of(session, scan));
        return scan.getStatus();
    }

    private void tryFetchBook(Scan scan, boolean lastTry) {
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
                if (lastTry) scan.markFailed();
                break;

        }
    }
}
