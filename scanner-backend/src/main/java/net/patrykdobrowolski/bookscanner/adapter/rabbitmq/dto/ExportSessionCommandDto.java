package net.patrykdobrowolski.bookscanner.adapter.rabbitmq.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Jacksonized
@Builder @Getter
public class ExportSessionCommandDto {

    private UUID sessionId;

    public static ExportSessionCommandDto forSession(UUID sessionId) {
        return new ExportSessionCommandDto(sessionId);
    }
}
