package net.patrykdobrowolski.bookshelf.adapter.exporter;

import net.patrykdobrowolski.bookshelf.domain.model.ExportFormat;

public class ExportFormatNotSupportedException extends Exception {

    public ExportFormatNotSupportedException(ExportFormat exportFormat) {
        super(exportFormat.name() + " not supported");
    }
}
