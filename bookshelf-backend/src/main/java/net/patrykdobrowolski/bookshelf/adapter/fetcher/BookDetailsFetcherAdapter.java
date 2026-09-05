package net.patrykdobrowolski.bookshelf.adapter.fetcher;

import jakarta.inject.Named;
import net.patrykdobrowolski.bookshelf.domain.model.fetch.BookFetchJob;
import net.patrykdobrowolski.bookshelf.domain.model.fetch.BookFetchResult;
import net.patrykdobrowolski.bookshelf.domain.model.fetch.ProviderFetchResult;
import net.patrykdobrowolski.bookshelf.domain.model.value.ISBN;
import net.patrykdobrowolski.bookshelf.domain.port.BookDetailsFetcherPort;
import net.patrykdobrowolski.bookshelf.domain.port.BookFetchJobRepositoryPort;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Named
public class BookDetailsFetcherAdapter implements BookDetailsFetcherPort {

    private final List<BookFetchProvider> providers;
    private final BookFetchJobRepositoryPort bookRepository;
    private final Executor apiFetchExecutor;

    public BookDetailsFetcherAdapter(
            List<BookFetchProvider> providers, BookFetchJobRepositoryPort bookRepository, @Qualifier("apiFetchExecutor") Executor apiFetchExecutor) {
        this.providers = providers;
        this.bookRepository = bookRepository;
        this.apiFetchExecutor = apiFetchExecutor;
    }

    @Override
    public BookFetchJob fetchBookDetails(ISBN isbn) {
        BookFetchJob bookFetchJob = bookRepository.findByISBN(isbn).orElseGet(() -> createEmpty(isbn));
        List<CompletableFuture<Void>> futures = bookFetchJob.getNewOrFailedRaws().stream()
                .map(bookRaw -> CompletableFuture.runAsync(() -> fetch(bookRaw, isbn), apiFetchExecutor))
                .toList();
        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
        bookRepository.save(bookFetchJob);
        return bookFetchJob;
    }

    private BookFetchJob createEmpty(ISBN isbn) {
        BookFetchJob bookFetchJob = BookFetchJob.from(isbn);
        for (BookFetchProvider provider : providers) {
            String key = provider.getKey();
            bookFetchJob.addEmptyRaw(key);
        }
        return bookFetchJob;
    }

    private void fetch(ProviderFetchResult providerFetchResult, ISBN isbn) {
        BookFetchProvider pro = providers.stream().filter(p -> p.getKey().equals(providerFetchResult.getSource())).findFirst().orElseThrow();
        BookFetchResult bookFetchResult = pro.fetchBookRaw(isbn);
        providerFetchResult.update(bookFetchResult);
    }
}
