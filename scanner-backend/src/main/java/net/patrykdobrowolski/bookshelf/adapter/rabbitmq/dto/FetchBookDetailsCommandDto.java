package net.patrykdobrowolski.bookshelf.adapter.rabbitmq.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Jacksonized
@Builder @Getter
public class FetchBookDetailsCommandDto {

    private UUID sessionId;
    private UUID scanId;
    private int tryCount;

    public static FetchBookDetailsCommandDto forScan(UUID sessionId, UUID scanId) {
        return new FetchBookDetailsCommandDto(sessionId, scanId, 0);
    }

    public void tried() {
        tryCount++;
    }
}
