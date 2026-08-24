package net.patrykdobrowolski.bookscanner.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
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
}
