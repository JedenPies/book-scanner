package net.patrykdobrowolski.bookshelf.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportAlreadyRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookshelf.domain.exception.DraftBookNotFoundException;
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
public class Session extends Aggregate {

    private UUID id;
    private Instant createdAt;
    private Instant lastUse;
    private List<DraftBook> draftBooks;
    private Export export;

    public static Session createNew() {
        Instant now = Instant.now();
        return Session.builder()
                .draftBooks(new ArrayList<>())
                .id(UUID.randomUUID())
                .createdAt(now)
                .lastUse(now)
                .build();
    }

    public DraftBook findScanById(UUID scanId) throws DraftBookNotFoundException {
        return draftBooks.stream().filter(scan -> Objects.equals(scan.getId(), scanId)).findFirst().orElseThrow(DraftBookNotFoundException::new);
    }

    public DraftBook createNewScan(ISBN isbn) {
        touch();
        DraftBook newDraftBook = DraftBook.createNew(isbn, this.id);
        DraftBook foundDraftBook = findOldestScanByIsbn(isbn);
        Optional.ofNullable(foundDraftBook).ifPresent(newDraftBook::copyDetails);
        draftBooks.add(newDraftBook);
        return newDraftBook;
    }

    public List<DraftBook> removeScans(List<UUID> scanIds) {
        touch();
        List<DraftBook> draftBooks = this.draftBooks.stream().filter(scan -> scanIds.contains(scan.getId())).toList();
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

    public DraftBook updateScan(UUID scanId, BookDetails newDetails) throws DraftBookNotFoundException {
        touch();
        DraftBook draftBook = findScanById(scanId);
        draftBook.setBookDetails(newDetails, Modifier.USER);
        return draftBook;
    }

    public DraftBook markScanFetching(UUID scanId) throws DraftBookNotFoundException {
        touch();
        DraftBook draftBook = findScanById(scanId);
        draftBook.markFetching();
        return draftBook;
    }

    public void markScanFailed(UUID scanId) throws DraftBookNotFoundException {
        touch();
        findScanById(scanId).markFailed();
    }

    public void markScanNotFound(UUID scanId) throws DraftBookNotFoundException {
        touch();
        findScanById(scanId).markNotFound();
    }

    public void setScanBookDetails(UUID scanId, BookDetails details, Modifier modifier) throws DraftBookNotFoundException {
        touch();
        findScanById(scanId).setBookDetails(details, modifier);
    }

    private void ensureExportExists() throws ExportNotRequestedException {
        if (export == null) throw new ExportNotRequestedException();
    }

    private DraftBook findOldestScanByIsbn(ISBN isbn) {
        return draftBooks.stream()
                .filter(scan -> Objects.equals(scan.getIsbn(), isbn))
                .min(Comparator.comparing(DraftBook::getCreatedAt)).orElse(null);
    }

    private void touch() {
        this.lastUse = Instant.now();
    }
}
