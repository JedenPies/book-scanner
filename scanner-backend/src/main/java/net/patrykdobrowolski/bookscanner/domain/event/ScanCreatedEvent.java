package net.patrykdobrowolski.bookscanner.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ScanCreatedEvent extends BusinessEvent {

    private final UUID sessionId;
    private final UUID scanId;

    public static ScanCreatedEvent of(UUID sessionId, UUID scanId) {
        return new ScanCreatedEvent(sessionId, scanId);
    }
}
