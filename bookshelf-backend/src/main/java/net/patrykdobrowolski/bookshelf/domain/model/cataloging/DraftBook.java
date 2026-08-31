package net.patrykdobrowolski.bookshelf.domain.model.cataloging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.patrykdobrowolski.bookshelf.domain.model.value.DraftBookStatus;
import net.patrykdobrowolski.bookshelf.domain.model.value.ISBN;
import net.patrykdobrowolski.bookshelf.domain.model.value.Modifier;
import net.patrykdobrowolski.bookshelf.domain.model.value.BookDetails;

import java.time.Instant;
import java.util.UUID;

@Builder @AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class DraftBook {

    private UUID id;
    private UUID sessionId;
    private DraftBookStatus status;
    private ISBN isbn;

    private BookDetails bookDetails;
    private Instant modifiedAt;
    private Instant createdAt;
    private Modifier modifiedBy;

    static DraftBook createNew(ISBN isbn, UUID sessionId) {
        return DraftBook.builder()
                .id(UUID.randomUUID())
                .sessionId(sessionId)
                .createdAt(Instant.now())
                .status(DraftBookStatus.PENDING)
                .isbn(isbn).build();
    }

    void copyDetails(DraftBook draftBook) {
        this.bookDetails = draftBook.getBookDetails();
        this.status = DraftBookStatus.DUPLICATE;
    }

    void markFetching() {
        this.status = DraftBookStatus.FETCHING;
    }

    void markFailed() {
        this.status = DraftBookStatus.FAILED;
    }

    void markNotFound() {
        this.status = DraftBookStatus.NOT_FOUND;
    }

    void setBookDetails(BookDetails details, Modifier modifier) {
        this.bookDetails = details;
        this.modifiedBy = modifier;
        this.status = DraftBookStatus.FOUND;
    }
}
