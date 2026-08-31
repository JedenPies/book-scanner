package net.patrykdobrowolski.bookscanner.adapter.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import net.patrykdobrowolski.bookscanner.domain.model.DraftBookStatus;

import java.time.Instant;
import java.util.UUID;

@Jacksonized
@Builder @Getter
public class DraftBookDto {

    private final UUID id;
    private final String isbn;
    private final DraftBookStatus status;
    private final BookDetailsDto bookDetails;
    private final Instant createdAt;
}
