package net.patrykdobrowolski.bookscanner.service;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.model.DraftBookStatus;
import net.patrykdobrowolski.bookscanner.domain.model.event.DraftBookCreatedEvent;
import net.patrykdobrowolski.bookscanner.domain.model.event.ExportRequestedEvent;
import net.patrykdobrowolski.bookscanner.domain.port.BookDetailsAsyncFetcherPort;
import net.patrykdobrowolski.bookscanner.domain.port.ExportCreatorAsyncPort;
import org.springframework.transaction.event.TransactionalEventListener;

@Named
@RequiredArgsConstructor
public class SessionApplicationEventsListener {

    private final BookDetailsAsyncFetcherPort bookDetailsFetcher;
    private final ExportCreatorAsyncPort exportCreator;

    @TransactionalEventListener
    public void onScanCreated(DraftBookCreatedEvent event) {
        if (event.getDraftBook().getStatus() == DraftBookStatus.PENDING)
            bookDetailsFetcher.fetchBookDetails(event.getSession(), event.getDraftBook());
    }

    @TransactionalEventListener
    public void onExportRequested(ExportRequestedEvent event) {
        exportCreator.exportSession(event.getSession());
    }

}
