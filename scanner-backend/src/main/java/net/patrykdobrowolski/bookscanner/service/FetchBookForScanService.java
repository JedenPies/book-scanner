package net.patrykdobrowolski.bookscanner.service;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.exception.CannotFetchBookException;
import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.*;
import net.patrykdobrowolski.bookscanner.domain.port.BookDetailsFetcherPort;
import net.patrykdobrowolski.bookscanner.fetcher.BookRawResultMapperAdapter;

import java.util.UUID;

@Named
@RequiredArgsConstructor
public class FetchBookForScanService {

    private final ScanService scanService;
    private final BookDetailsFetcherPort bookDetailsFetcher;
    private final BookRawResultMapperAdapter mapper;

    public void fetchBookForScan(UUID scanId) throws ScanNotFoundException {
        Scan scan = scanService.findScan(scanId);
        scan.markFetching();
        scanService.save(scan);
        tryFetchBook(scan);
        scanService.save(scan);
    }

    private void tryFetchBook(Scan scan) {
        try {
            Book book = bookDetailsFetcher.fetchBookDetails(scan.getIsbn());
            BookDetails details = mapper.map(book.getPreferededBookRaw());
            scan.setBookDetails(details, Modifier.SYSTEM);
        } catch (CannotFetchBookException e) {
            scan.markFailed();
        }
    }
}
