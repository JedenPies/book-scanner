package net.patrykdobrowolski.bookscanner.domain.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.domain.model.Session;

@RequiredArgsConstructor
@Getter
public class ScanCreatedEvent extends BusinessEvent {

    private final Session session;
    private final Scan scan;

    public static ScanCreatedEvent of(Session session, Scan scan) {
        return new ScanCreatedEvent(session, scan);
    }
}
