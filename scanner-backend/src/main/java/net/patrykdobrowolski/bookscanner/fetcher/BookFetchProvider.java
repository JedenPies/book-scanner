package net.patrykdobrowolski.bookscanner.fetcher;

import net.patrykdobrowolski.bookscanner.domain.model.BookRaw;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;

import java.util.Optional;

public interface BookFetchProvider {

    String getKey();
    Optional<BookRaw> fetchBookRaw(ISBN isbn);
}
