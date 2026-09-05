package net.patrykdobrowolski.bookshelf.adapter.fetcher;

import net.patrykdobrowolski.bookshelf.domain.model.fetch.BookFetchResult;
import net.patrykdobrowolski.bookshelf.domain.model.value.ISBN;

public interface BookFetchProvider {

    String getKey();
    BookFetchResult fetchBookRaw(ISBN isbn);
}
