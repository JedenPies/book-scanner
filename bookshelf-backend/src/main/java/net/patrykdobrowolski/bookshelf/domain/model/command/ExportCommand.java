package net.patrykdobrowolski.bookshelf.domain.model.command;

import lombok.Builder;
import lombok.Getter;
import lombok.With;
import net.patrykdobrowolski.bookshelf.domain.model.value.ExportFormat;
import net.patrykdobrowolski.bookshelf.domain.model.value.ExportType;

import java.util.UUID;

@Builder @Getter
public class ExportCommand {

    private ExportFormat format;
    @With
    private ExportType type;
    @With
    private UUID correlationKey;
}
