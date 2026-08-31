package net.patrykdobrowolski.bookshelf.domain.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.model.CatalogingSession;
import net.patrykdobrowolski.bookshelf.domain.model.DraftBook;

@RequiredArgsConstructor
@Getter
public class DraftBookCreatedEvent extends BusinessEvent {

    private final CatalogingSession catalogingSession;
    private final DraftBook draftBook;

    public static DraftBookCreatedEvent of(CatalogingSession catalogingSession, DraftBook draftBook) {
        return new DraftBookCreatedEvent(catalogingSession, draftBook);
    }
}
