package net.patrykdobrowolski.bookscanner.domain.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.model.DraftBook;
import net.patrykdobrowolski.bookscanner.domain.model.Session;

@RequiredArgsConstructor
@Getter
public class DraftBookCreatedEvent extends BusinessEvent {

    private final Session session;
    private final DraftBook draftBook;

    public static DraftBookCreatedEvent of(Session session, DraftBook draftBook) {
        return new DraftBookCreatedEvent(session, draftBook);
    }
}
