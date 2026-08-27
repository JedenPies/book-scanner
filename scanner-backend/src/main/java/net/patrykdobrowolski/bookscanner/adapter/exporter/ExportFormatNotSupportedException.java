package net.patrykdobrowolski.bookscanner.adapter.exporter;

import net.patrykdobrowolski.bookscanner.domain.model.ExportFormat;

public class ExportFormatNotSupportedException extends Exception {

    public ExportFormatNotSupportedException(ExportFormat exportFormat) {
        super(exportFormat.name() + " not supported");
    }
}
