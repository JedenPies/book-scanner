package net.patrykdobrowolski.bookscanner.fetcher;

import net.patrykdobrowolski.bookscanner.domain.model.BookRaw;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;

public interface BookFetchProvider {

    String getKey();
    BookRaw fetchBookRaw(ISBN isbn);
}
