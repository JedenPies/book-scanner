package net.patrykdobrowolski.bookscanner.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.model.Session;

@RequiredArgsConstructor @Getter
public class ExportRequestedEvent extends BusinessEvent {

    private final Session session;

    public static ExportRequestedEvent of(Session session) {
        return new ExportRequestedEvent(session);
    }
}
