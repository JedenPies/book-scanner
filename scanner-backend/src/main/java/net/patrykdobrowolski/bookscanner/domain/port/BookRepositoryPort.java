package net.patrykdobrowolski.bookscanner.domain.port;

import net.patrykdobrowolski.bookscanner.domain.exception.BookNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.Book;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;

public interface BookRepositoryPort {

    Book findByISBN(ISBN isbn) throws BookNotFoundException;
    Book save(Book book);
}
