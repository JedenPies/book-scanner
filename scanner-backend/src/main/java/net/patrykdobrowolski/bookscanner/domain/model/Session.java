package net.patrykdobrowolski.bookscanner.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.patrykdobrowolski.bookscanner.domain.model.command.ExportSessionCommand;
import net.patrykdobrowolski.bookscanner.domain.exception.ExportAlreadyRequestedException;
import net.patrykdobrowolski.bookscanner.domain.exception.ExportNotRequestedException;
import net.patrykdobrowolski.bookscanner.domain.exception.ScanNotFoundException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        return Session.builder()
                .scans(new ArrayList<>())
                .id(UUID.randomUUID())
                .createdAt(Instant.now())
                .build();
    }

    public Scan findScanById(UUID scanId) throws ScanNotFoundException {
        return scans.stream().filter(scan -> Objects.equals(scan.getId(), scanId)).findFirst().orElseThrow(ScanNotFoundException::new);
    }

    public Scan createNewScan(ISBN isbn) {
        Scan newScan = Scan.createNew(isbn, this.id);
        scans.add(newScan);
        return newScan;
    }

    public Scan removeScan(UUID scanId) throws ScanNotFoundException {
        Scan found = findScanById(scanId);
        scans.remove(found);
        return found;
    }

    public Export requestExport(ExportSessionCommand command) throws ExportAlreadyRequestedException {
        if (export != null && !export.isComplete()) {
            throw new ExportAlreadyRequestedException();
        }
        this.export = Export.createNew(command.getFormat());
        return this.export;
    }

    public void beginExport() throws ExportNotRequestedException {
        ensureExportExists();
        this.export.begin();
    }

    public void exportSucceed(byte[] data) throws ExportNotRequestedException {
        ensureExportExists();
        this.export.exported(data);
    }

    public void exportFailed() throws ExportNotRequestedException {
        ensureExportExists();
        this.export.failed();
    }

    private void ensureExportExists() throws ExportNotRequestedException {
        if (export == null) throw new ExportNotRequestedException();
    }

}
