package net.patrykdobrowolski.bookscanner.service;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.event.ScanCreatedEvent;
import net.patrykdobrowolski.bookscanner.domain.port.BookDetailsAsyncFetcherPort;
import org.springframework.transaction.event.TransactionalEventListener;

@Named
@RequiredArgsConstructor
public class BookScanApplicationEventListener {

    private final BookDetailsAsyncFetcherPort bookDetailsFetcher;

    @TransactionalEventListener
    public void onScanCreated(ScanCreatedEvent event) {
        bookDetailsFetcher.fetchBookDetails(event.getScanId());
    }
}
