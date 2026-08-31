package net.patrykdobrowolski.bookshelf.domain.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.model.DraftBook;
import net.patrykdobrowolski.bookshelf.domain.model.Session;

@RequiredArgsConstructor @Getter
public class DraftBookUpdatedEvent extends BusinessEvent {

    private final Session session;
    private final DraftBook draftBook;

    public static DraftBookUpdatedEvent of(Session session, DraftBook draftBook) {
        return new DraftBookUpdatedEvent(session, draftBook);
    }
}
