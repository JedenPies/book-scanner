package net.patrykdobrowolski.bookscanner.fetcher;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.exception.BookNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.service.BookService;
import org.springframework.core.annotation.Order;

import java.util.Optional;

@Named
@Order(10)
@RequiredArgsConstructor
public class LocalDatabaseBookDetailsFetchProvider implements BookDetailsFetchProvider {

    private final BookService bookService;

    @Override
    public String getKey() {
        return "local";
    }

    @Override
    public Optional<BookDetails> fetchBookDetails(ISBN isbn) {
        try {
            return Optional.of(bookService.findByISBN(isbn)).map(b -> b.getBookDetails().getLast().withSource("local"));
        } catch (BookNotFoundException e) {
            return Optional.empty();
        }
    }
}
