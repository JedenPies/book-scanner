package net.patrykdobrowolski.bookscanner.service;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.event.BookScanRequestedApplicationEvent;
import net.patrykdobrowolski.bookscanner.domain.port.BookDetailsAsyncFetcherPort;
import org.springframework.transaction.event.TransactionalEventListener;

@Named
@RequiredArgsConstructor
public class BookScanApplicationEventListener {

    private final BookDetailsAsyncFetcherPort bookDetailsFetcher;

    @TransactionalEventListener
    public void send(BookScanRequestedApplicationEvent event) {
        bookDetailsFetcher.fetchBookDetails(event.scanId(), event.isbn());
    }
}
