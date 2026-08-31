package net.patrykdobrowolski.bookshelf.domain.port;

import net.patrykdobrowolski.bookshelf.domain.exception.DraftBookNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.exception.SessionNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.DraftBook;
import net.patrykdobrowolski.bookshelf.domain.model.value.BookDetails;

import java.util.List;
import java.util.UUID;

public interface DraftBookServicePort {

    void retryDraftBookFetch(UUID sessionId, UUID draftBookId) throws DraftBookNotFoundException, SessionNotFoundException;
    List<DraftBook> getDraftBooks(UUID sessionId) throws SessionNotFoundException;
    DraftBook createDraftBook(UUID sessionId, String isbn) throws SessionNotFoundException;
    DraftBook updateDraftBook(UUID sessionId, UUID draftBookId, BookDetails newDetails) throws DraftBookNotFoundException, SessionNotFoundException;
    void deleteDraftBooks(UUID sessionId, List<UUID> draftBookIds) throws SessionNotFoundException;

}
