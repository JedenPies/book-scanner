package net.patrykdobrowolski.bookscanner.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.patrykdobrowolski.bookscanner.domain.model.command.UpdateScanCommand;

import java.time.Instant;
import java.util.Optional;
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
    private Instant createdAt;
    private Modifier modifiedBy;

    static Scan createNew(ISBN isbn, UUID sessionId) {
        return Scan.builder()
                .id(UUID.randomUUID())
                .sessionId(sessionId)
                .createdAt(Instant.now())
                .status(ScanStatus.PENDING)
                .isbn(isbn).build();
    }

    void copyDetails(Scan scan) {
        this.bookDetails = BookDetails.copyOf(scan.getBookDetails());
        this.status = ScanStatus.DUPLICATE;
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

    void update(UpdateScanCommand command) {
        this.status = ScanStatus.MODIFIED;
        this.modifiedAt = Instant.now();
        this.modifiedBy = Modifier.USER;
        this.bookDetails = Optional.ofNullable(this.bookDetails).orElseGet(BookDetails::empty);
        this.bookDetails.update(command);
    }
}
