package net.patrykdobrowolski.bookscanner.domain.model.command;

import lombok.Builder;
import lombok.Getter;
import net.patrykdobrowolski.bookscanner.domain.model.ExportFormat;

@Builder @Getter
public class ExportSessionCommand {

    private ExportFormat format;
}
