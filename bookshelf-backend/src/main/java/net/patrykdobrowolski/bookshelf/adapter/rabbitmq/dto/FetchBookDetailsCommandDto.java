package net.patrykdobrowolski.bookshelf.adapter.rabbitmq.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Jacksonized
@Builder @Getter
public class FetchBookDetailsCommandDto {

    private UUID sessionId;
    private UUID draftBookId;
    private int tryCount;

    public static FetchBookDetailsCommandDto forDraftBook(UUID sessionId, UUID draftBookId) {
        return new FetchBookDetailsCommandDto(sessionId, draftBookId, 0);
    }

    public void tried() {
        tryCount++;
    }
}
