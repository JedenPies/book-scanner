package net.patrykdobrowolski.bookscanner.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
public class Session {

    private UUID id;
    private Instant createdAt;
    private Instant lastUse;
}
