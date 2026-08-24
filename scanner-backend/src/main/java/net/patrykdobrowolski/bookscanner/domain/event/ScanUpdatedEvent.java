package net.patrykdobrowolski.bookscanner.domain.event;

import lombok.Builder;
import lombok.Getter;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;

@Builder @Getter
public class ScanUpdatedEvent extends BusinessEvent {

    private final Scan scan;
}
