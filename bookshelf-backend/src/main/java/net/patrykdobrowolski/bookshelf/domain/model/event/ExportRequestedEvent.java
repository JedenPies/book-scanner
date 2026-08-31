package net.patrykdobrowolski.bookshelf.domain.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.model.CatalogingSession;

@RequiredArgsConstructor @Getter
public class ExportRequestedEvent extends BusinessEvent {

    private final CatalogingSession catalogingSession;

    public static ExportRequestedEvent of(CatalogingSession catalogingSession) {
        return new ExportRequestedEvent(catalogingSession);
    }
}
