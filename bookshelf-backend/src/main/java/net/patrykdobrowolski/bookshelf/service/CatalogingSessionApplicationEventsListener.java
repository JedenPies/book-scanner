package net.patrykdobrowolski.bookshelf.service;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.model.value.DraftBookStatus;
import net.patrykdobrowolski.bookshelf.domain.model.event.DraftBookCreatedEvent;
import net.patrykdobrowolski.bookshelf.domain.model.event.ExportRequestedEvent;
import net.patrykdobrowolski.bookshelf.domain.port.BookDetailsAsyncFetcherPort;
import net.patrykdobrowolski.bookshelf.domain.port.ExportCreatorAsyncPort;
import org.springframework.transaction.event.TransactionalEventListener;

@Named
@RequiredArgsConstructor
public class CatalogingSessionApplicationEventsListener {

    private final BookDetailsAsyncFetcherPort bookDetailsFetcher;
    private final ExportCreatorAsyncPort exportCreator;

    @TransactionalEventListener
    public void onDraftBookCreated(DraftBookCreatedEvent event) {
        if (event.getDraftBook().getStatus() == DraftBookStatus.PENDING)
            bookDetailsFetcher.fetchBookDetails(event.getCatalogingSession(), event.getDraftBook());
    }

    @TransactionalEventListener
    public void onExportRequested(ExportRequestedEvent event) {
        exportCreator.exportSession(event.getCatalogingSession());
    }

}
