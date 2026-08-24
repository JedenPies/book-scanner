package net.patrykdobrowolski.bookscanner.rabbitmq.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Jacksonized
@Builder @Getter
public class FetchBookDetailsCommandDto {

    private UUID sessionId;
    private UUID scanId;

    public static FetchBookDetailsCommandDto forScan(UUID sessionId, UUID scanId) {
        return new FetchBookDetailsCommandDto(sessionId, scanId);
    }
}
