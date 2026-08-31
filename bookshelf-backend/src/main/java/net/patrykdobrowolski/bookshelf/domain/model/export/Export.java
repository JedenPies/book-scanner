package net.patrykdobrowolski.bookshelf.domain.model.export;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.patrykdobrowolski.bookshelf.domain.Aggregate;
import net.patrykdobrowolski.bookshelf.domain.model.command.ExportCommand;
import net.patrykdobrowolski.bookshelf.domain.model.value.ExportFormat;
import net.patrykdobrowolski.bookshelf.domain.model.value.ExportStatus;
import net.patrykdobrowolski.bookshelf.domain.model.value.ExportType;

import java.time.Instant;
import java.util.UUID;

@Builder
@AllArgsConstructor @Getter
public class Export extends Aggregate {

    private UUID id;
    private ExportFormat format;
    private ExportStatus status;
    private ExportType type;
    private UUID correlationKey;
    private byte[] data;
    private Instant createdAt;
    private Instant modifiedAt;

    public static Export createNew(ExportCommand command) {
        return Export.builder()
                .id(UUID.randomUUID())
                .format(command.getFormat())
                .type(command.getType())
                .correlationKey(command.getCorrelationKey())
                .createdAt(Instant.now())
                .status(ExportStatus.REQUESTED)
                .build();
    }

    public Export reset(ExportCommand command) {
        this.format = command.getFormat();
        this.status = ExportStatus.REQUESTED;
        this.type = command.getType();
        this.correlationKey = command.getCorrelationKey();
        this.createdAt = Instant.now();
        return this;
    }

    public boolean isComplete() {
        return ExportStatus.SUCCEED.equals(status) || ExportStatus.FAILED.equals(status);
    }

    public void begin() {
        this.status = ExportStatus.PROCESSING;
        this.modifiedAt = Instant.now();
    }

    public void exported(byte[] data) {
        this.data = data;
        this.status = ExportStatus.SUCCEED;
        this.modifiedAt = Instant.now();
    }

    public void failed() {
        this.status = ExportStatus.FAILED;
        this.modifiedAt = Instant.now();
    }
}
