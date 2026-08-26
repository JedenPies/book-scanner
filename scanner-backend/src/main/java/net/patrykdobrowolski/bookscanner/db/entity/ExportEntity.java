package net.patrykdobrowolski.bookscanner.db.entity;

import jakarta.persistence.*;
import lombok.*;
import net.patrykdobrowolski.bookscanner.domain.model.ExportFormat;
import net.patrykdobrowolski.bookscanner.domain.model.ExportStatus;

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

    private byte[] data;

    private Instant createdAt;

    private Instant modifiedAt;
}
