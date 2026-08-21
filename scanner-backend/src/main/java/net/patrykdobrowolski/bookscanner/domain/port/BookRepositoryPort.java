package net.patrykdobrowolski.bookscanner.domain.port;

import net.patrykdobrowolski.bookscanner.domain.model.Book;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;

import java.util.Optional;

public interface BookRepositoryPort {

    Optional<Book> findByISBN(ISBN isbn);
    Book save(Book book);
    Long count();
}
