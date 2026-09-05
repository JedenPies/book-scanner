package net.patrykdobrowolski.bookshelf.adapter.db.repository;

import net.patrykdobrowolski.bookshelf.adapter.db.entity.BookFetchJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataBookRepository extends JpaRepository<BookFetchJobEntity, UUID> {

    Optional<BookFetchJobEntity> findByIsbn(String isbn);
}
