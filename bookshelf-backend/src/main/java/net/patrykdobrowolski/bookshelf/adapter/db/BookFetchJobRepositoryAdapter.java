package net.patrykdobrowolski.bookshelf.adapter.db;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.adapter.db.entity.BookFetchJobEntity;
import net.patrykdobrowolski.bookshelf.adapter.db.mapper.BookEntityMapper;
import net.patrykdobrowolski.bookshelf.adapter.db.repository.SpringDataBookRepository;
import net.patrykdobrowolski.bookshelf.domain.model.fetch.BookFetchJob;
import net.patrykdobrowolski.bookshelf.domain.model.value.ISBN;
import net.patrykdobrowolski.bookshelf.domain.port.BookFetchJobRepositoryPort;

import java.util.Optional;

@Named
@RequiredArgsConstructor
public class BookFetchJobRepositoryAdapter implements BookFetchJobRepositoryPort {

    private final SpringDataBookRepository bookRepository;
    private final BookEntityMapper bookEntityMapper;

    @Override
    public Optional<BookFetchJob> findByISBN(ISBN isbn) {
        return bookRepository.findByIsbn(isbn.value()).map(bookEntityMapper::fromEntity);
    }

    @Override
    public BookFetchJob save(BookFetchJob bookFetchJob) {
        BookFetchJobEntity saved = bookRepository.save(bookEntityMapper.toEntity(bookFetchJob));
        return bookEntityMapper.fromEntity(saved);
    }

    @Override
    public Long count() {
        return bookRepository.count();
    }
}
