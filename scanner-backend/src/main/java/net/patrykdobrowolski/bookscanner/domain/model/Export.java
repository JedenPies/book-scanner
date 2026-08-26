package net.patrykdobrowolski.bookscanner.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Builder
@AllArgsConstructor @Getter
public class Export {

    private UUID id;
    private ExportFormat format;
    private ExportStatus status;
    private byte[] data;
    private Instant createdAt;
    private Instant modifiedAt;

    static Export createNew(ExportFormat format) {
        return Export.builder()
                .id(UUID.randomUUID())
                .format(format)
                .status(ExportStatus.REQUESTED)
                .createdAt(Instant.now())
                .build();
    }

    boolean isComplete() {
        return ExportStatus.COMPLETED.equals(status) || ExportStatus.FAILED.equals(status);
    }

    void begin() {
        this.status = ExportStatus.PENDING;
        this.modifiedAt = Instant.now();
    }

    void exported(byte[] data) {
        this.data = data;
        this.status = ExportStatus.COMPLETED;
        this.modifiedAt = Instant.now();
    }

    void failed() {
        this.status = ExportStatus.FAILED;
        this.modifiedAt = Instant.now();
    }
}
