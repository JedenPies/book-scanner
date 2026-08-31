package net.patrykdobrowolski.bookshelf.adapter.exporter;

import net.patrykdobrowolski.bookshelf.domain.model.value.ExportFormat;

public interface Exporter {

    boolean supports(ExportFormat format);
    ExportResult export(ExportData exportData) throws ExportFailedException;
}
