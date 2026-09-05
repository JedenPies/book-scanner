package net.patrykdobrowolski.bookshelf.domain.port;

import net.patrykdobrowolski.bookshelf.domain.model.fetch.BookFetchJob;
import net.patrykdobrowolski.bookshelf.domain.model.value.ISBN;

import java.util.Optional;

public interface BookFetchJobRepositoryPort {

    Optional<BookFetchJob> findByISBN(ISBN isbn);
    BookFetchJob save(BookFetchJob bookFetchJob);
    Long count();
}
