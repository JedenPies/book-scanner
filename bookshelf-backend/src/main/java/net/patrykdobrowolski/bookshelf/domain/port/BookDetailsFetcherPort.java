package net.patrykdobrowolski.bookshelf.domain.port;

import net.patrykdobrowolski.bookshelf.domain.model.Book;
import net.patrykdobrowolski.bookshelf.domain.model.value.ISBN;

public interface BookDetailsFetcherPort {

    Book fetchBookDetails(ISBN isbn);
}
