package net.patrykdobrowolski.bookscanner.domain.port;

import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.domain.model.Session;

public interface BookDetailsAsyncFetcherPort {

    void fetchBookDetails(Session session, Scan scan);
}
