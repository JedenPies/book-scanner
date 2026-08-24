package net.patrykdobrowolski.bookscanner.domain.event;

import lombok.Builder;
import lombok.Getter;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import net.patrykdobrowolski.bookscanner.domain.model.Session;

@Builder @Getter
public class ScanUpdatedEvent extends BusinessEvent {

    private final Session session;
    private final Scan scan;
}
