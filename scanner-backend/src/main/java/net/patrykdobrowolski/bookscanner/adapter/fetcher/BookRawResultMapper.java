package net.patrykdobrowolski.bookscanner.adapter.fetcher;

import net.patrykdobrowolski.bookscanner.domain.model.BookRaw;
import net.patrykdobrowolski.bookscanner.domain.model.value.BookDetails;

public interface BookRawResultMapper {

    String getKey();
    BookDetails map(BookRaw bookRaw);
}
