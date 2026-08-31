package net.patrykdobrowolski.bookscanner.domain.port;

import net.patrykdobrowolski.bookscanner.domain.model.DraftBook;
import net.patrykdobrowolski.bookscanner.domain.model.Session;

public interface BookDetailsAsyncFetcherPort {

    void fetchBookDetails(Session session, DraftBook draftBook);
}
