package net.patrykdobrowolski.bookshelf.adapter.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import net.patrykdobrowolski.bookshelf.domain.model.ExportFormat;
import net.patrykdobrowolski.bookshelf.domain.model.ExportStatus;

import java.time.Instant;
import java.util.UUID;

@Jacksonized
@Builder @Getter
public class ExportDto {

    private final UUID id;
    private ExportFormat format;
    private ExportStatus status;
    private Instant createdAt;
}
