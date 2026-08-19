package net.patrykdobrowolski.bookscanner.db.adapter;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.db.entity.BookEntity;
import net.patrykdobrowolski.bookscanner.db.mapper.BookEntityMapper;
import net.patrykdobrowolski.bookscanner.db.repository.SpringDataBookRepository;
import net.patrykdobrowolski.bookscanner.domain.exception.BookNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.Book;
import net.patrykdobrowolski.bookscanner.domain.model.ISBN;
import net.patrykdobrowolski.bookscanner.domain.port.BookRepositoryPort;

@Named
@RequiredArgsConstructor
public class BookRepositoryAdapter implements BookRepositoryPort {

    private final SpringDataBookRepository bookRepository;
    private final BookEntityMapper bookEntityMapper;

    @Override
    public Book findByISBN(ISBN isbn) throws BookNotFoundException {
        return bookRepository.findByIsbn(isbn.value()).map(bookEntityMapper::fromEntity).orElseThrow(BookNotFoundException::new);
    }

    @Override
    public Book save(Book book) {
        BookEntity saved = bookRepository.save(bookEntityMapper.toEntity(book));
        return bookEntityMapper.fromEntity(saved);
    }
}
