package net.patrykdobrowolski.bookshelf.domain.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.model.DraftBook;
import net.patrykdobrowolski.bookshelf.domain.model.Session;

import java.util.List;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class DraftBooksDeletedEvent extends BusinessEvent {

    private final Session session;
    private final List<DraftBook> draftBooks;
}
