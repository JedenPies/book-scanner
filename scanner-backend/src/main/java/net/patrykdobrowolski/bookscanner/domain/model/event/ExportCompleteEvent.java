package net.patrykdobrowolski.bookscanner.domain.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.model.Export;
import net.patrykdobrowolski.bookscanner.domain.model.Session;

@RequiredArgsConstructor @Getter
public class ExportCompleteEvent extends BusinessEvent {

    private final Session session;
    private final Export export;

    public static ExportCompleteEvent of(Session session, Export export) {
        return new ExportCompleteEvent(session, export);
    }
}
