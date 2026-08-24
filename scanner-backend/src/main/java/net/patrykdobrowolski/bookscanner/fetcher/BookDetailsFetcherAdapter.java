package net.patrykdobrowolski.bookscanner.fetcher;

import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import net.patrykdobrowolski.bookscanner.domain.exception.CannotFetchBookException;
import net.patrykdobrowolski.bookscanner.domain.model.Book;
import net.patrykdobrowolski.bookscanner.domain.model.BookRaw;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.domain.port.BookDetailsFetcherPort;
import net.patrykdobrowolski.bookscanner.domain.port.BookRepositoryPort;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Named
public class BookDetailsFetcherAdapter implements BookDetailsFetcherPort {

    private final List<BookFetchProvider> providers;
    private final BookRepositoryPort bookRepository;
    private final Executor apiFetchExecutor;

    public BookDetailsFetcherAdapter(
            List<BookFetchProvider> providers, BookRepositoryPort bookRepository, @Qualifier("apiFetchExecutor") Executor apiFetchExecutor) {
        this.providers = providers;
        this.bookRepository = bookRepository;
        this.apiFetchExecutor = apiFetchExecutor;
    }

    @Override
    @Transactional
    public Book fetchBookDetails(ISBN isbn) throws CannotFetchBookException {
        Book book = bookRepository.findByISBN(isbn).orElseGet(() -> fetchAndCreate(isbn));
        if (book == null) throw new CannotFetchBookException();
        return book;
    }

    private @Nullable Book fetchAndCreate(ISBN isbn) {
        List<BookRaw> bookRawsFromAdapters = bookDetailsFromAdapters(isbn);
        if (!bookRawsFromAdapters.isEmpty()) {
            Book book = Book.from(isbn);
            book.addRaws(bookRawsFromAdapters);
            return bookRepository.save(book);
        }
        return null;
    }

    private List<BookRaw> bookDetailsFromAdapters(ISBN isbn) {
        List<CompletableFuture<BookRaw>> futures = providers.stream()
                .map(provider -> fetchWithProvider(provider, isbn))
                .toList();
        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    private CompletableFuture<BookRaw> fetchWithProvider(BookFetchProvider provider, ISBN isbn) {
        return CompletableFuture.supplyAsync(() -> fetch(provider, isbn).withSource(provider.getKey()), apiFetchExecutor);
    }

    private BookRaw fetch(BookFetchProvider provider, ISBN isbn) {
        return provider.fetchBookRaw(isbn); //.map(bookRaw -> bookRaw.withSource(provider.getKey()));
    }
}
