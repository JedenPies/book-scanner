package net.patrykdobrowolski.bookshelf.adapter.fetcher;

import net.patrykdobrowolski.bookshelf.domain.model.BookRaw;
import net.patrykdobrowolski.bookshelf.domain.model.value.BookDetails;

public interface BookRawResultMapper {

    String getKey();
    BookDetails map(BookRaw bookRaw);
}
