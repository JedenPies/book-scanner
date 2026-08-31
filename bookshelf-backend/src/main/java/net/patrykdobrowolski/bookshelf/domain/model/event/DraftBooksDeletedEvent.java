package net.patrykdobrowolski.bookshelf.domain.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.model.cataloging.CatalogingSession;
import net.patrykdobrowolski.bookshelf.domain.model.cataloging.DraftBook;

import java.util.List;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class DraftBooksDeletedEvent extends BusinessEvent {

    private final CatalogingSession catalogingSession;
    private final List<DraftBook> draftBooks;
}
