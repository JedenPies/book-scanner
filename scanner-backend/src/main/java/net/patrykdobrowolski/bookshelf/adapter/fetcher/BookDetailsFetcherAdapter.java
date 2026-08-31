package net.patrykdobrowolski.bookshelf.adapter.fetcher;

import jakarta.inject.Named;
import net.patrykdobrowolski.bookshelf.domain.model.Book;
import net.patrykdobrowolski.bookshelf.domain.model.BookFetchResult;
import net.patrykdobrowolski.bookshelf.domain.model.BookRaw;
import net.patrykdobrowolski.bookshelf.domain.model.ISBN;
import net.patrykdobrowolski.bookshelf.domain.port.BookDetailsFetcherPort;
import net.patrykdobrowolski.bookshelf.domain.port.BookRepositoryPort;
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
    public Book fetchBookDetails(ISBN isbn) {
        Book book = bookRepository.findByISBN(isbn).orElseGet(() -> createEmpty(isbn));
        List<CompletableFuture<Void>> futures = book.getNewOrFailedRaws().stream()
                .map(bookRaw -> CompletableFuture.runAsync(() -> fetch(bookRaw, isbn), apiFetchExecutor))
                .toList();
        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
        bookRepository.save(book);
        return book;
    }

    private Book createEmpty(ISBN isbn) {
        Book book = Book.from(isbn);
        for (BookFetchProvider provider : providers) {
            String key = provider.getKey();
            book.addEmptyRaw(key);
        }
        return book;
    }

    private void fetch(BookRaw bookRaw, ISBN isbn) {
        BookFetchProvider pro = providers.stream().filter(p -> p.getKey().equals(bookRaw.getSource())).findFirst().orElseThrow();
        BookFetchResult bookFetchResult = pro.fetchBookRaw(isbn);
        bookRaw.update(bookFetchResult);
    }
}
