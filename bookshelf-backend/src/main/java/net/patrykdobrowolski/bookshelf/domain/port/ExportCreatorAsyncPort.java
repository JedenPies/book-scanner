package net.patrykdobrowolski.bookshelf.domain.port;

import net.patrykdobrowolski.bookshelf.domain.model.cataloging.CatalogingSession;

public interface ExportCreatorAsyncPort {

    void exportSession(CatalogingSession catalogingSession);
}
