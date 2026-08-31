package net.patrykdobrowolski.bookshelf.domain.port;

import net.patrykdobrowolski.bookshelf.domain.exception.DraftBookNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.exception.CatalogingSessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.value.DraftBookStatus;

import java.util.UUID;

public interface FetchBookServicePort {

    DraftBookStatus fetchBookForDraft(UUID sessionId, UUID draftBookId, boolean lastTry) throws DraftBookNotFoundException, CatalogingSessionNotFoundException;
}
