package net.patrykdobrowolski.bookscanner.domain.port;

import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;

import java.util.UUID;

public interface BookDetailsFetcherPort {

    BookDetails fetchBookDetails(UUID scanId, ISBN isbn) throws ScanNotFoundException;
}
