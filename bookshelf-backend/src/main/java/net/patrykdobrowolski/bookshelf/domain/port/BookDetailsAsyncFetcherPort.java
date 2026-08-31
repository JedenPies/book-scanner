package net.patrykdobrowolski.bookshelf.domain.port;

import net.patrykdobrowolski.bookshelf.domain.model.DraftBook;
import net.patrykdobrowolski.bookshelf.domain.model.CatalogingSession;

public interface BookDetailsAsyncFetcherPort {

    void fetchBookDetails(CatalogingSession catalogingSession, DraftBook draftBook);
}
