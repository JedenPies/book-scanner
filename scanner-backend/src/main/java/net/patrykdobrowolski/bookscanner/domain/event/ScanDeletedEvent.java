package net.patrykdobrowolski.bookscanner.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.domain.model.Session;

@RequiredArgsConstructor @Getter
public class ScanDeletedEvent extends BusinessEvent {

    private final Session session;
    private final Scan scan;

    public static ScanDeletedEvent of(Session session, Scan scan) {
        return new ScanDeletedEvent(session, scan);
    }
}
