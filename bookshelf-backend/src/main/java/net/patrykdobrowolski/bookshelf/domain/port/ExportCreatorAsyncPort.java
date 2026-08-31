package net.patrykdobrowolski.bookshelf.domain.port;

import net.patrykdobrowolski.bookshelf.domain.model.export.Export;

public interface ExportCreatorAsyncPort {

    void export(Export export);
}
