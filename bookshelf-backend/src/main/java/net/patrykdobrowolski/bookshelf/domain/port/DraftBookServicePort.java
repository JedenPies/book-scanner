package net.patrykdobrowolski.bookshelf.domain.port;

import net.patrykdobrowolski.bookshelf.domain.exception.DraftBookNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.exception.CatalogingSessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.cataloging.DraftBook;
import net.patrykdobrowolski.bookshelf.domain.model.value.BookDetails;

import java.util.List;
import java.util.UUID;

public interface DraftBookServicePort {

    void retryDraftBookFetch(UUID sessionId, UUID draftBookId) throws DraftBookNotFoundException, CatalogingSessionNotFoundException;
    List<DraftBook> getDraftBooks(UUID sessionId) throws CatalogingSessionNotFoundException;
    DraftBook createDraftBook(UUID sessionId, String isbn) throws CatalogingSessionNotFoundException;
    DraftBook updateDraftBook(UUID sessionId, UUID draftBookId, BookDetails newDetails) throws DraftBookNotFoundException, CatalogingSessionNotFoundException;
    void deleteDraftBooks(UUID sessionId, List<UUID> draftBookIds) throws CatalogingSessionNotFoundException;

}
