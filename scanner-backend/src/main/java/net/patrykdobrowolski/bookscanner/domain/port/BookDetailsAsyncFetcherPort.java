package net.patrykdobrowolski.bookscanner.domain.port;

import net.patrykdobrowolski.bookscanner.domain.model.ISBN;

import java.util.UUID;

public interface BookDetailsAsyncFetcherPort {

    void fetchBookDetails(UUID scanId, ISBN isbn);
}
