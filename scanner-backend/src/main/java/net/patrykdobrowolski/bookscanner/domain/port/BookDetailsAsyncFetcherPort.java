package net.patrykdobrowolski.bookscanner.domain.port;

import java.util.UUID;

public interface BookDetailsAsyncFetcherPort {

    void fetchBookDetails(UUID scanId);
}
