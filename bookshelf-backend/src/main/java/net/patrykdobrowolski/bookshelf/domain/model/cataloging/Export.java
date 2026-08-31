package net.patrykdobrowolski.bookshelf.domain.model.cataloging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.patrykdobrowolski.bookshelf.domain.model.value.ExportFormat;
import net.patrykdobrowolski.bookshelf.domain.model.value.ExportStatus;

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
        return ExportStatus.SUCCEED.equals(status) || ExportStatus.FAILED.equals(status);
    }

    void begin() {
        this.status = ExportStatus.PROCESSING;
        this.modifiedAt = Instant.now();
    }

    void exported(byte[] data) {
        this.data = data;
        this.status = ExportStatus.SUCCEED;
        this.modifiedAt = Instant.now();
    }

    void failed() {
        this.status = ExportStatus.FAILED;
        this.modifiedAt = Instant.now();
    }
}
