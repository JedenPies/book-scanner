package net.patrykdobrowolski.bookshelf.domain.port;

import net.patrykdobrowolski.bookshelf.domain.model.cataloging.DraftBook;
import net.patrykdobrowolski.bookshelf.domain.model.cataloging.CatalogingSession;

public interface BookDetailsAsyncFetcherPort {

    void fetchBookDetails(CatalogingSession catalogingSession, DraftBook draftBook);
}
