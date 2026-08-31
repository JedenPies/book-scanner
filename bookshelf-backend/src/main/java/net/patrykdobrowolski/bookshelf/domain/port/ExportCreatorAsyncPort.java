package net.patrykdobrowolski.bookshelf.domain.port;

import net.patrykdobrowolski.bookshelf.domain.model.CatalogingSession;

public interface ExportCreatorAsyncPort {

    void exportSession(CatalogingSession catalogingSession);
}
