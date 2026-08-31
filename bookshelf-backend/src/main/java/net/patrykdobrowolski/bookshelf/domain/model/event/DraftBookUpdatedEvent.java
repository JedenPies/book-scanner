package net.patrykdobrowolski.bookshelf.domain.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.model.cataloging.CatalogingSession;
import net.patrykdobrowolski.bookshelf.domain.model.cataloging.DraftBook;

@RequiredArgsConstructor @Getter
public class DraftBookUpdatedEvent extends BusinessEvent {

    private final CatalogingSession catalogingSession;
    private final DraftBook draftBook;

    public static DraftBookUpdatedEvent of(CatalogingSession catalogingSession, DraftBook draftBook) {
        return new DraftBookUpdatedEvent(catalogingSession, draftBook);
    }
}
