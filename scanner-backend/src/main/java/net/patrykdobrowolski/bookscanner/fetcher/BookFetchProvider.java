package net.patrykdobrowolski.bookscanner.fetcher;

import net.patrykdobrowolski.bookscanner.domain.model.BookFetchResult;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;

public interface BookFetchProvider {

    String getKey();
    BookFetchResult fetchBookRaw(ISBN isbn);
}
