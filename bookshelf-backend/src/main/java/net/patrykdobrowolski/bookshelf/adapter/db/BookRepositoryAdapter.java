package net.patrykdobrowolski.bookshelf.adapter.db;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.adapter.db.entity.BookEntity;
import net.patrykdobrowolski.bookshelf.adapter.db.mapper.BookEntityMapper;
import net.patrykdobrowolski.bookshelf.adapter.db.repository.SpringDataBookRepository;
import net.patrykdobrowolski.bookshelf.domain.model.Book;
import net.patrykdobrowolski.bookshelf.domain.model.ISBN;
import net.patrykdobrowolski.bookshelf.domain.port.BookRepositoryPort;

import java.util.Optional;

@Named
@RequiredArgsConstructor
public class BookRepositoryAdapter implements BookRepositoryPort {

    private final SpringDataBookRepository bookRepository;
    private final BookEntityMapper bookEntityMapper;

    @Override
    public Optional<Book> findByISBN(ISBN isbn) {
        return bookRepository.findByIsbn(isbn.value()).map(bookEntityMapper::fromEntity);
    }

    @Override
    public Book save(Book book) {
        BookEntity saved = bookRepository.save(bookEntityMapper.toEntity(book));
        return bookEntityMapper.fromEntity(saved);
    }

    @Override
    public Long count() {
        return bookRepository.count();
    }
}
