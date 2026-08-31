package net.patrykdobrowolski.bookshelf.domain.model.command;

import lombok.Builder;
import lombok.Getter;
import net.patrykdobrowolski.bookshelf.domain.model.ExportFormat;

@Builder @Getter
public class ExportSessionCommand {

    private ExportFormat format;
}
