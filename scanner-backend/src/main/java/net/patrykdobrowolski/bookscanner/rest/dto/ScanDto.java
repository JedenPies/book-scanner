package net.patrykdobrowolski.bookscanner.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import net.patrykdobrowolski.bookscanner.domain.model.ScanStatus;

import java.time.Instant;
import java.util.UUID;

@Jacksonized
@Builder @Getter
public class ScanDto {

    private final UUID id;
    private final String isbn;
    private final ScanStatus status;
    private final BookDetailsDto bookDetails;
    private final Instant createdAt;
}
