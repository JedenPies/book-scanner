package net.patrykdobrowolski.bookscanner.adapter.fetcher;

import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import net.patrykdobrowolski.bookscanner.domain.model.BookRaw;

public interface BookRawResultMapper {

    String getKey();
    BookDetails map(BookRaw bookRaw);
}
