package net.patrykdobrowolski.bookscanner.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Builder @AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class Scan {

    private UUID id;
    private UUID sessionId;
    private ScanStatus status;
    private ISBN isbn;

    private BookDetails bookDetails;
    private Instant modifiedAt;
    private Modifier modifiedBy;

    public Scan(UUID sessionId, ISBN isbn) {
        this.sessionId = sessionId;
        this.status = ScanStatus.PENDING;
        this.isbn = isbn;
    }

    public void markFetching() {
        this.status = ScanStatus.FETCHING;
    }

    public void markFailed() {
        this.status = ScanStatus.FAILED;
    }

    public void markNotFound() {
        this.status = ScanStatus.NOT_FOUND;
    }

    public void setBookDetails(BookDetails details, Modifier modifier) {
        this.bookDetails = details;
        this.modifiedBy = modifier;
        this.status = ScanStatus.FOUND;
    }
}
