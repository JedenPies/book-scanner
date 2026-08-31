package net.patrykdobrowolski.bookshelf.domain.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.model.cataloging.Export;
import net.patrykdobrowolski.bookshelf.domain.model.cataloging.CatalogingSession;

@RequiredArgsConstructor @Getter
public class ExportCompleteEvent extends BusinessEvent {

    private final CatalogingSession catalogingSession;
    private final Export export;

    public static ExportCompleteEvent of(CatalogingSession catalogingSession, Export export) {
        return new ExportCompleteEvent(catalogingSession, export);
    }
}
