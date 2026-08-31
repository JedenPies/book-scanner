package net.patrykdobrowolski.bookscanner.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.patrykdobrowolski.bookscanner.domain.exception.ExportAlreadyRequestedException;
import net.patrykdobrowolski.bookscanner.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;
import net.patrykdobrowolski.bookscanner.domain.model.command.ExportSessionCommand;
import net.patrykdobrowolski.bookscanner.domain.model.value.BookDetails;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Builder @AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class Session {

    private UUID id;
    private Instant createdAt;
    private Instant lastUse;
    private List<Scan> scans;
    private Export export;

    public static Session createNew() {
        Instant now = Instant.now();
        return Session.builder()
                .scans(new ArrayList<>())
                .id(UUID.randomUUID())
                .createdAt(now)
                .lastUse(now)
                .build();
    }

    public Scan findScanById(UUID scanId) throws ScanNotFoundException {
        return scans.stream().filter(scan -> Objects.equals(scan.getId(), scanId)).findFirst().orElseThrow(ScanNotFoundException::new);
    }

    public Scan createNewScan(ISBN isbn) {
        touch();
        Scan newScan = Scan.createNew(isbn, this.id);
        Scan foundScan = findOldestScanByIsbn(isbn);
        Optional.ofNullable(foundScan).ifPresent(newScan::copyDetails);
        scans.add(newScan);
        return newScan;
    }

    public List<Scan> removeScans(List<UUID> scanIds) {
        touch();
        List<Scan> scans = this.scans.stream().filter(scan -> scanIds.contains(scan.getId())).toList();
        scans.forEach(this.scans::remove);
        return scans;
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

    public Scan updateScan(UUID scanId, BookDetails newDetails) throws ScanNotFoundException {
        touch();
        Scan scan = findScanById(scanId);
        scan.setBookDetails(newDetails, Modifier.USER);
        return scan;
    }

    public Scan markScanFetching(UUID scanId) throws ScanNotFoundException {
        touch();
        Scan scan = findScanById(scanId);
        scan.markFetching();
        return scan;
    }

    public void markScanFailed(UUID scanId) throws ScanNotFoundException {
        touch();
        findScanById(scanId).markFailed();
    }

    public void markScanNotFound(UUID scanId) throws ScanNotFoundException {
        touch();
        findScanById(scanId).markNotFound();
    }

    public void setScanBookDetails(UUID scanId, BookDetails details, Modifier modifier) throws ScanNotFoundException {
        touch();
        findScanById(scanId).setBookDetails(details, modifier);
    }

    private void ensureExportExists() throws ExportNotRequestedException {
        if (export == null) throw new ExportNotRequestedException();
    }

    private Scan findOldestScanByIsbn(ISBN isbn) {
        return scans.stream()
                .filter(scan -> Objects.equals(scan.getIsbn(), isbn))
                .min(Comparator.comparing(Scan::getCreatedAt)).orElse(null);
    }

    private void touch() {
        this.lastUse = Instant.now();
    }
}
