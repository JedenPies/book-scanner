package net.patrykdobrowolski.bookscanner.domain.port;

import net.patrykdobrowolski.bookscanner.domain.model.Book;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;

public interface BookDetailsFetcherPort {

    Book fetchBookDetails(ISBN isbn);
}
