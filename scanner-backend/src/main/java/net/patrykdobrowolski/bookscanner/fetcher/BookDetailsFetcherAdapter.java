package net.patrykdobrowolski.bookscanner.fetcher;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.domain.port.BookDetailsFetcherPort;
import net.patrykdobrowolski.bookscanner.service.BookService;
import net.patrykdobrowolski.bookscanner.service.ScanService;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Named
@RequiredArgsConstructor
public class BookDetailsFetcherAdapter implements BookDetailsFetcherPort {

    private final List<BookDetailsFetchProvider> providers;
    private final BookService bookService;
    private final ScanService scanService;

    @Override
    public BookDetails fetchBookDetails(UUID scanId, ISBN isbn) throws ScanNotFoundException {

        Scan scan = scanService.findScan(scanId);
        scan.markFetching();
        scanService.save(scan);
        BookDetails bookDetails = bookDetailsFromAdapters(isbn);
        if (bookDetails != null && !bookDetails.isLocal()) {
            bookService.createBook(isbn, bookDetails);
        }
        return bookDetails;
    }

    private @Nullable BookDetails bookDetailsFromAdapters(ISBN isbn) {
        for (BookDetailsFetchProvider provider : providers) {
            Optional<BookDetails> fetchedDetails = provider.fetchBookDetails(isbn);
            if (fetchedDetails.isPresent()) {
                return fetchedDetails.get().withSource(provider.getKey());
            }
        }
        return null;
    }
}
