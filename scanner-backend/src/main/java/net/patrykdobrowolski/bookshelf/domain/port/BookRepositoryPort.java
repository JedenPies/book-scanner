package net.patrykdobrowolski.bookshelf.domain.port;

import net.patrykdobrowolski.bookshelf.domain.model.Book;
import net.patrykdobrowolski.bookshelf.domain.model.ISBN;

import java.util.Optional;

public interface BookRepositoryPort {

    Optional<Book> findByISBN(ISBN isbn);
    Book save(Book book);
    Long count();
}
