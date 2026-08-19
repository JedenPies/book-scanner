package net.patrykdobrowolski.bookscanner.fetcher;

import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;

import java.util.Optional;

public interface BookDetailsFetchProvider {

    String getKey();
    Optional<BookDetails> fetchBookDetails(ISBN isbn);
}
