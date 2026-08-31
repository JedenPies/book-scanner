package net.patrykdobrowolski.bookshelf.adapter.db.entity;

import jakarta.persistence.*;
import lombok.*;
import net.patrykdobrowolski.bookshelf.domain.model.value.ExportFormat;
import net.patrykdobrowolski.bookshelf.domain.model.value.ExportStatus;
import net.patrykdobrowolski.bookshelf.domain.model.value.ExportType;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "exports")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ExportEntity {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ExportFormat format;

    @Enumerated(EnumType.STRING)
    private ExportStatus status;

    @Enumerated(EnumType.STRING)
    private ExportType type;

    private UUID correlationKey;

    private byte[] data;

    private Instant createdAt;

    private Instant modifiedAt;
}
