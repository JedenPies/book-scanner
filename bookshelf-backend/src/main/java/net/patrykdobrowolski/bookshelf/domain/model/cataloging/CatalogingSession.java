package net.patrykdobrowolski.bookshelf.domain.model.cataloging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.patrykdobrowolski.bookshelf.domain.Aggregate;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportAlreadyRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.DraftBookNotFoundException;
import net.patrykdobrowolski.bookshelf.domain.model.value.ISBN;
import net.patrykdobrowolski.bookshelf.domain.model.value.Modifier;
import net.patrykdobrowolski.bookshelf.domain.model.command.ExportSessionCommand;
import net.patrykdobrowolski.bookshelf.domain.model.value.BookDetails;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Builder @AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class CatalogingSession extends Aggregate {

    private UUID id;
    private Instant createdAt;
    private Instant lastUse;
    private List<DraftBook> draftBooks;
    private Export export;

    public static CatalogingSession createNew() {
        Instant now = Instant.now();
        return CatalogingSession.builder()
                .draftBooks(new ArrayList<>())
                .id(UUID.randomUUID())
                .createdAt(now)
                .lastUse(now)
                .build();
    }

    public DraftBook findDraftBookById(UUID draftBookId) throws DraftBookNotFoundException {
        return draftBooks.stream().filter(draftBook -> Objects.equals(draftBook.getId(), draftBookId)).findFirst().orElseThrow(DraftBookNotFoundException::new);
    }

    public DraftBook createNewDraftBook(ISBN isbn) {
        touch();
        DraftBook newDraftBook = DraftBook.createNew(isbn, this.id);
        DraftBook foundDraftBook = findOldestDraftBookByIsbn(isbn);
        Optional.ofNullable(foundDraftBook).ifPresent(newDraftBook::copyDetails);
        draftBooks.add(newDraftBook);
        return newDraftBook;
    }

    public List<DraftBook> removeDraftBooks(List<UUID> draftBooksIds) {
        touch();
        List<DraftBook> draftBooks = this.draftBooks.stream().filter(draftBook -> draftBooksIds.contains(draftBook.getId())).toList();
        draftBooks.forEach(this.draftBooks::remove);
        return draftBooks;
    }

    public Export requestExport(ExportSessionCommand command) throws ExportAlreadyRequestedException {
        touch();
        if (export != null && !export.isComplete()) {
            throw new ExportAlreadyRequestedException();
        }
        this.export = Export.createNew(command.getFormat());
        return this.export;
    }

    public void beginExport() throws ExportNotRequestedException {
        touch();
        ensureExportExists();
        this.export.begin();
    }

    public void exportSucceed(byte[] data) throws ExportNotRequestedException {
        touch();
        ensureExportExists();
        this.export.exported(data);
    }

    public void exportFailed() throws ExportNotRequestedException {
        touch();
        ensureExportExists();
        this.export.failed();
    }

    public DraftBook updateDraftBook(UUID draftBookId, BookDetails newDetails) throws DraftBookNotFoundException {
        touch();
        DraftBook draftBook = findDraftBookById(draftBookId);
        draftBook.setBookDetails(newDetails.withSources(draftBook.getBookDetails().sources()), Modifier.USER);
        return draftBook;
    }

    public DraftBook markDraftBookFetching(UUID draftBookId) throws DraftBookNotFoundException {
        touch();
        DraftBook draftBook = findDraftBookById(draftBookId);
        draftBook.markFetching();
        return draftBook;
    }

    public void markDraftBookFailed(UUID draftBookId) throws DraftBookNotFoundException {
        touch();
        findDraftBookById(draftBookId).markFailed();
    }

    public void markDraftBookNotFound(UUID draftBookId) throws DraftBookNotFoundException {
        touch();
        findDraftBookById(draftBookId).markNotFound();
    }

    public void setDraftBookBookDetails(UUID draftBookId, BookDetails details, Modifier modifier) throws DraftBookNotFoundException {
        touch();
        findDraftBookById(draftBookId).setBookDetails(details, modifier);
    }

    private void ensureExportExists() throws ExportNotRequestedException {
        if (export == null) throw new ExportNotRequestedException();
    }

    private DraftBook findOldestDraftBookByIsbn(ISBN isbn) {
        return draftBooks.stream()
                .filter(draftBook -> Objects.equals(draftBook.getIsbn(), isbn))
                .min(Comparator.comparing(DraftBook::getCreatedAt)).orElse(null);
    }

    private void touch() {
        this.lastUse = Instant.now();
    }
}
