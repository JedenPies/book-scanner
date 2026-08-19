package net.patrykdobrowolski.bookscanner.service;

import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.exception.BookNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.Book;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.domain.port.BookRepositoryPort;

import java.util.Collections;

@Named
@RequiredArgsConstructor
public class BookService {

    private final BookRepositoryPort bookRepository;

    @Transactional
    public Book findByISBN(ISBN isbn) throws BookNotFoundException {
        return bookRepository.findByISBN(isbn);
    }

    @Transactional
    public Book createBook(ISBN isbn, BookDetails details) {
        Book newBook = Book.builder()
                .isbn(isbn)
                .bookDetails(Collections.singletonList(details))
                .build();
        return bookRepository.save(newBook);
    }
}
