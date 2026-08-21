package net.patrykdobrowolski.bookscanner.domain.event;

import net.patrykdobrowolski.bookscanner.domain.model.ISBN;

import java.util.UUID;

public record BookScanRequestedApplicationEvent(ISBN isbn, UUID scanId) {

    public static BookScanRequestedApplicationEvent of(ISBN isbn, UUID scanId) {
        return new BookScanRequestedApplicationEvent(isbn, scanId);
    }
}
