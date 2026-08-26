package net.patrykdobrowolski.bookscanner.service;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.event.ExportRequestedEvent;
import net.patrykdobrowolski.bookscanner.domain.event.ScanCreatedEvent;
import net.patrykdobrowolski.bookscanner.domain.port.BookDetailsAsyncFetcherPort;
import net.patrykdobrowolski.bookscanner.domain.port.ExportCreatorAsyncPort;
import org.springframework.transaction.event.TransactionalEventListener;

@Named
@RequiredArgsConstructor
public class SessionApplicationEventsListener {

    private final BookDetailsAsyncFetcherPort bookDetailsFetcher;
    private final ExportCreatorAsyncPort exportCreator;

    @TransactionalEventListener
    public void onScanCreated(ScanCreatedEvent event) {
        bookDetailsFetcher.fetchBookDetails(event.getSession(), event.getScan());
    }

    @TransactionalEventListener
    public void onExportRequested(ExportRequestedEvent event) {
        exportCreator.exportSession(event.getSession());
    }

}
